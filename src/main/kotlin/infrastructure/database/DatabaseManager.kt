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
import entity.medicaldevice.MedicalDeviceData
import entity.patient.PatientData
import entity.process.ProcessData
import entity.process.SurgicalProcess
import java.time.Instant

/**
 * Implementation of all database managers.
 */
class DatabaseManager(
    connectionString: String
) : MedicalDeviceDatabaseManager, PatientMedicalDataDatabaseManager, ProcessDatabaseManager {
    init {
        check(connectionString.isNotEmpty()) {
            "Please provide the Staff Tracking MongoDB connection string!"
        }
    }

    override fun addMedicalDeviceUsage(
        medicalDeviceId: MedicalDeviceData.ImplantableMedicalDeviceId,
        processId: ProcessData.ProcessId
    ): Boolean {
        TODO("Not yet implemented")
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
}
