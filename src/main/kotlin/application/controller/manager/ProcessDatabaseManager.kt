/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application.controller.manager

import usecase.repository.SurgicalProcessRepository

/**
 * This interface models the manager of the database for processes.
 */
interface ProcessDatabaseManager : SurgicalProcessRepository
