package ggaworowski.worktime.data

import android.support.test.filters.LargeTest
import android.support.test.runner.AndroidJUnit4
import ggaworowski.worktime.data.repositories.WorkTimeRepository
import ggaworowski.worktime.model.WorkEventModel

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import io.realm.Realm
import org.junit.Assert
import java.util.*

/**
 * Created by gregor on 18.03.18.
 */

@RunWith(AndroidJUnit4::class)
@LargeTest
class WorkTimeRepositoryDataTest {

    val workTimeRepository = WorkTimeRepository()

    @Before
    fun setup() {
        Realm.getDefaultInstance().use {
            it.executeTransaction({
                it.deleteAll()
            })
        }
    }

    @Test
    fun testMultipleInsertion() {
        for (i in 1..10) {
            workTimeRepository.insertOrUpdate(WorkEventModel(true, Date())).blockingFirst()
        }
        val actualState = workTimeRepository.getActualWorkState().blockingFirst()
        Assert.assertTrue(actualState.isWorkOn)

        Assert.assertTrue(workTimeRepository.getWorkHistoryForDay(Date()).blockingFirst().list.size == 1)

        for (i in 1..10) {
            workTimeRepository.insertOrUpdate(WorkEventModel(false, Date())).blockingFirst()
        }

        Assert.assertTrue(workTimeRepository.getWorkHistoryForDay(Date()).blockingFirst().list.size == 1)

        for (i in 1..10) {
            workTimeRepository.insertOrUpdate(WorkEventModel(true, Date())).blockingFirst()
            workTimeRepository.insertOrUpdate(WorkEventModel(false, Date())).blockingFirst()
        }

        Assert.assertTrue(workTimeRepository.getWorkHistoryForDay(Date()).blockingFirst().list.size == 11)
    }
}
