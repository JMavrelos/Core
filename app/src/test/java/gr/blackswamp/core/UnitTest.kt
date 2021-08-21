@file:Suppress("unused")

package gr.blackswamp.core

import android.app.Application
import androidx.annotation.CallSuper
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.reset
import com.nhaarman.mockitokotlin2.whenever
import gr.blackswamp.core.logging.LogCat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mockito.mock

abstract class UnitTest {
    companion object {
        const val APP_STRING = "message"
    }

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Suppress("EXPERIMENTAL_API_USAGE")
    @get:Rule
    var coroutineTestRule = CoroutineTestRule()

    protected open val stringInjections = mapOf<Int, String>()
    protected val app: Application = mock(Application::class.java)

    @Suppress("EXPERIMENTAL_API_USAGE")
    private val dispatcher = TestCoroutineDispatcher()

    @Before
    @CallSuper
    open fun setup() {
        reset(app)
        LogCat.enable(debug = false, testing = true, "UnitTests")
        setUpApplicationMocks()
        @Suppress("EXPERIMENTAL_API_USAGE")
        Dispatchers.setMain(dispatcher)
    }

    private fun setUpApplicationMocks() {
        whenever(app.getString(anyInt())).then { mock ->
            val id = mock.arguments.first() as Int
            stringInjections.entries.firstOrNull { it.key == id }?.value ?: APP_STRING
        }
        whenever(app.getString(anyInt(), any())).thenReturn(APP_STRING)
    }
}