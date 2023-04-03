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
import com.mongodb.MongoException
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import entity.medicaldevice.MedicalDeviceData
import entity.patient.PatientData
import entity.process.ProcessData
import entity.process.SurgicalProcess
import org.litote.kmongo.KMongo
import org.litote.kmongo.getCollection
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

    private val implantableMedicalDeviceCollection =
        this.database.getCollection<MedicalDeviceUsage>(implantableMedicalDeviceCollectionName)

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
    ): Boolean {
        TODO("Not yet implemented")
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
        to: Instant
    ): List<Pair<Instant, PatientData.MedicalData>> {
        TODO("Not yet implemented")
    }

    override fun getCurrentPatientMedicalData(patientId: PatientData.PatientId): PatientData.MedicalData? {
        TODO("Not yet implemented")
    }

    override fun createSurgicalProcess(process: SurgicalProcess): SurgicalProcess? {
        TODO("Not yet implemented")
    }

    override fun getSurgicalProcessById(processId: ProcessData.ProcessId): SurgicalProcess? {
        TODO("Not yet implemented")
    }

    override fun getCurrentSurgicalProcesses(): Set<SurgicalProcess> {
        TODO("Not yet implemented")
    }

    override fun updateSurgicalProcessState(
        processId: ProcessData.ProcessId,
        state: ProcessData.ProcessState
    ): Boolean {
        TODO("Not yet implemented")
    }

    override fun updateSurgicalProcessStep(processId: ProcessData.ProcessId, step: ProcessData.ProcessStep): Boolean {
        TODO("Not yet implemented")
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
//        private const val medicalTechnologyUsageDataCollectionName = "medical_technology_usage_data"
        private const val implantableMedicalDeviceCollectionName = "implantable_medical_device"
//        const val processStateEventsTimeSeriesCollectionName = "process_state_events"
//        const val processStepEventsTimeSeriesCollectionName = "process_step_events"
//        private const val patientMedicalDataTimeSeriesCollectionName = "patient_medical_data"
//        private const val surgicalProcessCollectionName = "surgical_process"
    }
}
