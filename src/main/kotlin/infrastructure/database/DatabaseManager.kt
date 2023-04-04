/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package infrastructure.database

import application.controller.manager.MedicalDeviceDatabaseManager
import application.controller.manager.PatientMedicalDataDatabaseManager
import application.controller.manager.ProcessDatabaseManager
import application.presenter.database.model.MedicalDeviceUsage
import application.presenter.database.model.TimeSeriesMedicalTechnologyUsage
import application.presenter.database.model.TimeSeriesMedicalTechnologyUsageMetadata
import application.presenter.database.model.TimeSeriesPatientMedicalData
import application.presenter.database.model.TimeSeriesPatientMedicalDataMetadata
import application.presenter.database.model.TimeSeriesProcessStateEvent
import application.presenter.database.model.TimeSeriesProcessStepEvent
import application.presenter.database.model.toPatientMedicalData
import com.mongodb.MongoException
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import entity.medicaldevice.MedicalDeviceData
import entity.patient.PatientData
import entity.process.ProcessData
import entity.process.SurgicalProcess
import org.litote.kmongo.KMongo
import org.litote.kmongo.ascendingSort
import org.litote.kmongo.div
import org.litote.kmongo.eq
import org.litote.kmongo.find
import org.litote.kmongo.findOne
import org.litote.kmongo.getCollection
import org.litote.kmongo.gt
import org.litote.kmongo.lte
import org.litote.kmongo.ne
import org.litote.kmongo.updateOne
import java.time.Instant

/**
 * Implementation of all database managers.
 */
class DatabaseManager(
    connectionString: String
) : MedicalDeviceDatabaseManager, PatientMedicalDataDatabaseManager, ProcessDatabaseManager {

    /**
     * The mongodb client.
     */
    val database: MongoDatabase = KMongo.createClient(connectionString).getDatabase(databaseName)

    private val medicalTechnologyUsageDataCollection =
        this.database.getCollection<TimeSeriesMedicalTechnologyUsage>(medicalTechnologyUsageDataCollectionName)

    private val patientMedicalDataCollection =
        this.database.getCollection<TimeSeriesPatientMedicalData>(patientMedicalDataTimeSeriesCollectionName)

    private val implantableMedicalDeviceCollection =
        this.database.getCollection<MedicalDeviceUsage>(implantableMedicalDeviceCollectionName)

    private val surgicalProcessCollection =
        this.database.getCollection<SurgicalProcess>(surgicalProcessCollectionName)

    private val processStateEventCollection =
        this.database.getCollection<TimeSeriesProcessStateEvent>(processStateEventsTimeSeriesCollectionName)

    private val processStepEventCollection =
        this.database.getCollection<TimeSeriesProcessStepEvent>(processStepEventsTimeSeriesCollectionName)

    override fun addMedicalDeviceUsage(
        medicalDeviceId: MedicalDeviceData.ImplantableMedicalDeviceId,
        processId: ProcessData.ProcessId
    ): Boolean = this.implantableMedicalDeviceCollection.safeMongoDbWrite(defaultResult = false) {
        insertOne(MedicalDeviceUsage(medicalDeviceId, processId)).wasAcknowledged()
    }

    override fun addMedicalTechnologyUsage(
        medicalTechnologyId: MedicalDeviceData.MedicalTechnologyId,
        dateTime: Instant,
        processId: ProcessData.ProcessId,
        inUse: Boolean
    ): Boolean = this.medicalTechnologyUsageDataCollection.safeMongoDbWrite(defaultResult = false) {
        insertOne(
            TimeSeriesMedicalTechnologyUsage(
                dateTime,
                TimeSeriesMedicalTechnologyUsageMetadata(medicalTechnologyId, processId),
                inUse
            )
        ).wasAcknowledged()
    }

    override fun updatePatientMedicalData(
        patientId: PatientData.PatientId,
        medicalData: PatientData.MedicalData,
        dateTime: Instant
    ): Boolean {
        TODO("Not yet implemented")
    }

    override fun getPatientMedicalData(
        patientId: PatientData.PatientId,
        from: Instant,
        to: Instant,
    ): List<Pair<Instant, PatientData.MedicalData>> {
        var patientMedicalData = PatientData.MedicalData()
        return this.patientMedicalDataCollection.find(
            TimeSeriesPatientMedicalData::metadata / TimeSeriesPatientMedicalDataMetadata::patientId eq patientId,
            TimeSeriesPatientMedicalData::dateTime gt from,
            TimeSeriesPatientMedicalData::dateTime lte to
        ).ascendingSort(TimeSeriesPatientMedicalData::dateTime).toList().map {
            val updatedPatientMedicalData = mapOf(it.metadata.type to it).toPatientMedicalData(patientMedicalData)
            patientMedicalData = updatedPatientMedicalData
            it.dateTime to updatedPatientMedicalData
        }
    }

    override fun getCurrentPatientMedicalData(patientId: PatientData.PatientId): PatientData.MedicalData {
        var patientMedicalData = PatientData.MedicalData()
        return this.patientMedicalDataCollection.find(
            TimeSeriesPatientMedicalData::metadata / TimeSeriesPatientMedicalDataMetadata::patientId eq patientId,
        ).ascendingSort(TimeSeriesPatientMedicalData::dateTime).toList().map {
            val updatedPatientMedicalData = mapOf(it.metadata.type to it).toPatientMedicalData(patientMedicalData)
            patientMedicalData = updatedPatientMedicalData
            updatedPatientMedicalData
        }.first()
    }

    override fun createSurgicalProcess(process: SurgicalProcess): SurgicalProcess? =
        this.surgicalProcessCollection.safeMongoDbWrite(defaultResult = null) {
            insertOne(
                process
            ).run {
                getSurgicalProcessById(process.id)
            }
        }

    override fun getSurgicalProcessById(processId: ProcessData.ProcessId): SurgicalProcess? =
        this.surgicalProcessCollection.findOne(SurgicalProcess::id eq processId)

    override fun getCurrentSurgicalProcesses(): Set<SurgicalProcess> =
        this.surgicalProcessCollection.find(
            SurgicalProcess::state ne ProcessData.ProcessState.TERMINATED
        ).toSet()

    override fun updateSurgicalProcessState(
        processId: ProcessData.ProcessId,
        dateTime: Instant,
        state: ProcessData.ProcessState
    ): Boolean =
        this.processStateEventCollection.safeMongoDbWrite(defaultResult = false) {
            updateOne(SurgicalProcess::id eq processId).wasAcknowledged()
        }

    override fun updateSurgicalProcessStep(
        processId: ProcessData.ProcessId,
        dateTime: Instant,
        step: ProcessData.ProcessStep
    ): Boolean =
        this.processStepEventCollection.safeMongoDbWrite(defaultResult = false) {
            updateOne(SurgicalProcess::id eq processId).wasAcknowledged()
        }

    private fun <T, R> MongoCollection<T>.safeMongoDbWrite(defaultResult: R, operation: MongoCollection<T>.() -> R): R =
        try {
            operation()
        } catch (exception: MongoException) {
            println(exception)
            defaultResult
        }

    companion object {
        private const val databaseName = "staff_tracking"
        private const val medicalTechnologyUsageDataCollectionName = "medical_technology_usage_data"
        private const val implantableMedicalDeviceCollectionName = "implantable_medical_device"
        private const val processStateEventsTimeSeriesCollectionName = "process_state_events"
        private const val processStepEventsTimeSeriesCollectionName = "process_step_events"
        private const val patientMedicalDataTimeSeriesCollectionName = "patient_medical_data"
        private const val surgicalProcessCollectionName = "surgical_process"
    }
}
