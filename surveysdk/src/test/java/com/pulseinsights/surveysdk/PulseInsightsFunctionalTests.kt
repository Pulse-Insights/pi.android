package com.pulseinsights.surveysdk

import android.content.Context
import android.hardware.SensorManager
import android.os.CountDownTimer
import android.text.TextUtils
import android.util.Log
import com.pulseinsights.surveysdk.data.model.Survey
import com.pulseinsights.surveysdk.jsontool.HttpCore
import com.pulseinsights.surveysdk.jsontool.JsonGetResult
import com.pulseinsights.surveysdk.util.PreferencesManager
import io.mockk.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.junit.Assert.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@RunWith(JUnit4::class)
class PulseInsightsFunctionalTests {
    private val mockContext = mockk<Context>(relaxed = true)
    private val mockPreferencesManager = mockk<PreferencesManager>()
    private val mockSensorManager = mockk<SensorManager>()
    private lateinit var pulseInsights: PulseInsights

    init {
        mockkStatic(Log::class)
        every { Log.i(any(), any()) } returns 0
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
        every { Log.v(any(), any()) } returns 0
    }

    @Before
    fun setUp() {
        clearAllMocks()

        // Create a mock CountDownTimer that does nothing
        val mockTimer = object : CountDownTimer(1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {}
        }

        // Set the mock timer before initializing PulseInsights
        LocalData.instant.apply {
            strCheckingSurveyId = ""
            strSubmitId = ""
            strUdid = ""
            strAccountId = ""
            surveyPack = Survey()
            surveyTickets.clear()
            mirrorTimer = mockTimer
        }

        // Mock all PreferencesManager methods comprehensively using both property and method syntax
        mockkStatic(PreferencesManager::class)
        every { PreferencesManager.getInstance(any()) } returns mockPreferencesManager
        
        // Server host
        every { mockPreferencesManager.serverHost } returns "https://test-host.example.com"
        every { mockPreferencesManager.getServerHost() } returns "https://test-host.example.com"
        every { mockPreferencesManager.changeHostUrl(any()) } just Runs
        
        // Debug mode
        every { mockPreferencesManager.isDebugModeEnable } returns false
        every { mockPreferencesManager.isDebugModeEnable() } returns false
        every { mockPreferencesManager.changeDebugModeSetting(any()) } just Runs
        
        // Client key
        every { mockPreferencesManager.clientKey } returns ""
        every { mockPreferencesManager.getClientKey() } returns ""
        every { mockPreferencesManager.setClientKey(any()) } just Runs
        
        // Launch count
        every { mockPreferencesManager.launchCount } returns 1
        every { mockPreferencesManager.getLaunchCount() } returns 1
        every { mockPreferencesManager.addLaunchCount() } just Runs
        
        // Device UDID
        every { mockPreferencesManager.deviceUdid } returns "test-device-udid"
        every { mockPreferencesManager.getDeviceUdid() } returns "test-device-udid"
        every { mockPreferencesManager.saveDeviceUdid(any()) } just Runs
        every { mockPreferencesManager.resetDeviceUdid() } just Runs
        
        // Survey answered
        every { mockPreferencesManager.isSurveyAnswered(any()) } returns false
        every { mockPreferencesManager.logAnsweredSurvey(any()) } just Runs

        // Mock HttpCore
        mockkConstructor(HttpCore::class)
        every { anyConstructed<HttpCore>().setCallBack(any()) } answers {
            // Capture the callback directly here and store it for later use
//            val callback = firstArg<JsonGetResult>()
//            every { anyConstructed<HttpCore>().c } returns callback
//            Unit
        }
        every { anyConstructed<HttpCore>().composeRequestUrl(any(), any()) } returns "https://test-host.example.com/serve?param=value"
        every { anyConstructed<HttpCore>().startRequest(any(), any()) } just Runs

        // Mock TextUtils
        mockkStatic(android.text.TextUtils::class)
        every { TextUtils.isEmpty(any()) } answers {
            val str = it.invocation.args[0] as? CharSequence
            str == null || str.isEmpty()
        }

        // Mock SensorManager
        every { mockContext.getSystemService(Context.SENSOR_SERVICE) } returns mockSensorManager
        every { mockSensorManager.getDefaultSensor(any()) } returns null

        // Mock Package and Resource
        every { mockContext.getPackageName() } returns "com.pulseinsights.surveysdk.test"
        every { mockContext.getResources() } returns mockk(relaxed = true)
        every { mockContext.getAssets() } returns mockk(relaxed = true)

        // Mock Toast
        mockkStatic(android.widget.Toast::class)
        every { android.widget.Toast.makeText(any(), any<String>(), any()) } returns mockk(relaxed = true)

        // Now initialize PulseInsights
        pulseInsights = PulseInsights(mockContext, "test-account-id")
    }

    @After
    fun tearDown() {
        unmockkAll()

        LocalData.instant.apply {
            strCheckingSurveyId = ""
            strSubmitId = ""
            strUdid = ""
            strAccountId = ""
            surveyPack = Survey()
            surveyTickets.clear()
        }
    }

    // MARK: Initial and config
    @Test
    fun testInitialization_ShouldSetCorrectDefaults() {
        assertEquals("test-account-id", LocalData.instant.strAccountId)
        assertTrue(LocalData.instant.surveyWatcherEnable)
        assertFalse(LocalData.instant.previewMode)
        assertTrue(LocalData.instant.customData.isEmpty())
    }

    @Test
    fun testInitialization_WithCustomParameters_ShouldSetCorrectValues() {
        val customData = mapOf("key1" to "value1", "key2" to "value2")
        val config = ExtraConfig().apply {
            this.automaticStart = false
            this.previewMode = true
            this.customData = customData
        }
        val pulseInsights = PulseInsights(mockContext, "test-account-id", config)

        assertEquals("test-account-id", LocalData.instant.strAccountId)
        assertFalse(LocalData.instant.surveyWatcherEnable)
        assertTrue(LocalData.instant.previewMode)
        assertEquals(customData, LocalData.instant.customData)
    }

    @Test
    fun testConfigAccountID_ShouldUpdateAccountID() {
        pulseInsights.configAccountId("new-account-id")
        assertEquals("new-account-id", LocalData.instant.strAccountId)
    }

    @Test
    fun testSetHost_ShouldUpdateHostURL() {
        pulseInsights.setHost("test-host.example.new.com")

        verify {
            mockPreferencesManager.changeHostUrl(match { url ->
                url == "https://test-host.example.new.com" ||
                        url == "test-host.example.new.com"
            })
        }
    }

    /**
     * Helper function to create a CountDownTimer for mocking
     */
    private fun createCountDownTimer(millisInFuture: Long, countDownInterval: Long): CountDownTimer {
        return object : CountDownTimer(millisInFuture, countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {}
        }
    }

    // MARK: Context and Device Data
    @Test
    fun testSetContextData_WithMergeTrue_ShouldMergeWithExistingData() {
        val initialData = mapOf("key1" to "value1", "key2" to "value2")
        pulseInsights.setContextData(initialData, true)

        val newData = mapOf("key2" to "updated", "key3" to "value3")
        pulseInsights.setContextData(newData, true)

        val expectedData = mapOf("key1" to "value1", "key2" to "updated", "key3" to "value3")
        assertEquals(expectedData, LocalData.instant.customData)
    }

    @Test
    fun testSetContextData_WithMergeFalse_ShouldReplaceExistingData() {
        val initialData = mapOf("key1" to "value1", "key2" to "value2")
        pulseInsights.setContextData(initialData, false)

        val newData = mapOf("key3" to "value3", "key4" to "value4")
        pulseInsights.setContextData(newData, false)

        assertEquals(newData, LocalData.instant.customData)
    }

    @Test
    fun testClearContextData_ShouldRemoveAllContextData() {
        val initialData = mapOf("key1" to "value1", "key2" to "value2")
        pulseInsights.setContextData(initialData, false)

        pulseInsights.clearContextData()

        assertTrue(LocalData.instant.customData.isEmpty())
    }

    // MARK: Preview Mode
    @Test
    fun testSetPreviewMode_ShouldUpdatePreviewMode() {
        pulseInsights.setPreviewMode(true)
        assertTrue(LocalData.instant.previewMode)

        pulseInsights.setPreviewMode(false)
        assertFalse(LocalData.instant.previewMode)
    }

    @Test
    fun testIsPreviewModeOn_ShouldReturnCorrectState() {
        assertFalse(pulseInsights.isPreviewModeOn())
        
        pulseInsights.setPreviewMode(true)
        assertTrue(pulseInsights.isPreviewModeOn())
    }

    // MARK: serve
    @Test
    fun testServe_ShouldTriggerAPIRequest() {
        // Arrange
        val mockApi = mockk<PulseInsightsApi>(relaxed = true)
        every { mockApi.serve() } just Runs

        val field = PulseInsights::class.java.getDeclaredField("pulseInsightsApi")
        field.isAccessible = true
        field.set(pulseInsights, mockApi)

        // Act
        pulseInsights.serve()

        // Assert
        verify { mockApi.serve() }
    }
} 