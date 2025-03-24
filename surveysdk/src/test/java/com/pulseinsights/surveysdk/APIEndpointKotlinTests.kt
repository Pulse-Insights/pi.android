package com.pulseinsights.surveysdk

import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.pulseinsights.surveysdk.data.model.Survey
import com.pulseinsights.surveysdk.jsontool.HttpCore
import com.pulseinsights.surveysdk.jsontool.JsonGetResult
import com.pulseinsights.surveysdk.util.EventListener
import com.pulseinsights.surveysdk.util.PreferencesManager
import com.pulseinsights.surveysdk.util.SurveyFlowResult
import io.mockk.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class APIEndpointKotlinTests {
    private val mockContext = mockk<Context>(relaxed = true)
    private val mockPreferencesManager = mockk<PreferencesManager>()

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

        LocalData.instant.apply {
            strCheckingSurveyId = ""
            strSubmitId = ""
            strUdid = ""
            strAccountId = ""
            surveyPack = Survey()
            surveyTickets.clear()
        }

        mockkStatic(PreferencesManager::class)
        every { PreferencesManager.getInstance(any()) } returns mockPreferencesManager
        every { mockPreferencesManager.getServerHost() } returns "https://test-host.example.com"
        every { mockPreferencesManager.isDebugModeEnable() } returns false
        every { mockPreferencesManager.getClientKey() } returns ""
        every { mockPreferencesManager.getLaunchCount() } returns 1

        mockkConstructor(HttpCore::class)
        every { anyConstructed<HttpCore>().setCallBack(any()) } just Runs
        every {
            anyConstructed<HttpCore>().composeRequestUrl(
                any(),
                any<Map<String, String>>()
            )
        } returns "https://test-host.example.com/serve?param=value"
        every { anyConstructed<HttpCore>().startRequest(any(), any()) } just Runs

        mockkStatic(android.text.TextUtils::class)
        every { TextUtils.isEmpty(any()) } answers {
            val str = it.invocation.args[0] as? CharSequence
            str == null || str.isEmpty()
        }

        println("Test setup completed with clean state")
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

        println("Test teardown completed with clean state")
    }

    // MARK: - Serve API
    @Test
    fun testServeAPI_WhenSuccessful_ShouldParseResponse() {
        val mockResponse = """
            {
                "survey": {
                    "id": "7498",
                    "name": "Test Survey"
                },
                "submission": {
                    "udid": "test-submission-id"
                }
            }
        """.trimIndent()

        val latch = CountDownLatch(1)
        val success = booleanArrayOf(false)

        val httpCoreSlot = slot<JsonGetResult>()
        every { anyConstructed<HttpCore>().setCallBack(capture(httpCoreSlot)) } just Runs

        val api = PulseInsightsApi(mockContext, object : SurveyFlowResult {
            override fun result(strType: String, strExtend: String) {
                // this is not a typo, at least not in here..
                if (strType == Define.SURVEY_REQ_TYPE_SCAN && strExtend == "sucess") {
                    success[0] = true
                }
                latch.countDown()
            }
        })

        verify { anyConstructed<HttpCore>().setCallBack(any()) }

        api.serve()

        verify {
            anyConstructed<HttpCore>().composeRequestUrl("serve", withArg { params ->
                assertTrue("should contain udid", params.containsKey("udid"))
                assertTrue("should contain device_type", params.containsKey("device_type"))
                assertTrue("should contain launch_times ", params.containsKey("launch_times"))
                assertEquals("launch_times should be 1", "1", params["launch_times"])
                true
            })
        }

        verify { anyConstructed<HttpCore>().startRequest(any(), Define.SURVEY_REQ_TYPE_SCAN) }

        httpCoreSlot.captured.getResult(mockResponse, Define.SURVEY_REQ_TYPE_SCAN)

        val completed = latch.await(2, TimeUnit.SECONDS)

        assertTrue("Asynchronous operation should complete before timeout", completed)
        assertTrue("API call should be successful", success[0])

        assertEquals(
            "Survey ID should be correctly parsed",
            "7498",
            LocalData.instant.surveyPack.survey.id
        )
        assertEquals(
            "Submission ID should be correctly parsed",
            "test-submission-id",
            LocalData.instant.strSubmitId
        )
    }

    @Test
    fun testServeAPI_WhenServerReturnsError_ShouldReturnFalse() {
        val latch = CountDownLatch(1)
        val success = booleanArrayOf(false)
        val receivedErrorSignal = booleanArrayOf(false)

        val httpCoreSlot = slot<JsonGetResult>()
        every { anyConstructed<HttpCore>().setCallBack(capture(httpCoreSlot)) } just Runs

        every { anyConstructed<HttpCore>().startRequest(any(), any()) } just Runs

        val api = PulseInsightsApi(mockContext, object : SurveyFlowResult {
            override fun result(strType: String, strExtend: String) {
                println("Received result: type=$strType, extend=$strExtend")
                if (strType == "error") {
                    receivedErrorSignal[0] = true
                } else if (strType == Define.SURVEY_REQ_TYPE_SCAN && strExtend == "sucess") {
                    success[0] = true
                }
                latch.countDown()
            }
        })

        api.serve()

        httpCoreSlot.captured.getResult("", "error")

        val completed = latch.await(5, TimeUnit.SECONDS)

        assertTrue("Asynchronous operation should complete before timeout", completed)
        assertTrue("Should receive error signal", receivedErrorSignal[0])
        assertFalse("API call should fail", success[0])
    }

    // MARK: - setDeviceData API
    @Test
    fun testSetDeviceData_ShouldSendCorrectRequest() {
        val deviceData = mapOf(
            "device_name" to "Pixel 4",
            "os_version" to "Android 11"
        )

        val urlSlot = slot<String>()
        val requestTypeSlot = slot<String>()
        val requestCallbackSlot = slot<JsonGetResult>()

        every { anyConstructed<HttpCore>().setCallBack(capture(requestCallbackSlot)) } just Runs
        every {
            anyConstructed<HttpCore>().composeRequestUrl(
                any(),
                any<Map<String, String>>()
            )
        } returns "https://test-host.example.com/devices/test-udid/set_data"
        every {
            anyConstructed<HttpCore>().startRequest(
                capture(urlSlot),
                capture(requestTypeSlot)
            )
        } just Runs

        LocalData.instant.strUdid = "test-udid"
        LocalData.instant.strAccountId = "test-account"

        val api = PulseInsightsApi(mockContext)

        api.setDeviceData(deviceData)

        verify {
            anyConstructed<HttpCore>().composeRequestUrl(match { path ->
                path.contains("devices/test-udid/set_data")
            }, withArg { params ->
                assertTrue("Should contain device_name", params.containsKey("device_name"))
                assertEquals("Should have correct device_name", "Pixel 4", params["device_name"])
                assertTrue("Should contain os_version", params.containsKey("os_version"))
                assertEquals("Should have correct os_version", "Android 11", params["os_version"])
                assertTrue("Should contain identifier", params.containsKey("identifier"))
                assertEquals("Should have correct identifier", "test-account", params["identifier"])
                true
            })
        }

        verify { anyConstructed<HttpCore>().startRequest(any(), "set_data") }

        assertTrue("URL should contain device UDID", urlSlot.captured.contains("test-udid"))
        assertEquals("Request type should be set_data", "set_data", requestTypeSlot.captured)
    }

    //  MARK: - getSurveyInformation API
    @Test
    fun testGetSurveyInformation_WithValidId_ShouldParseResponse() {
        val mockResponse = """
            {
                "survey": {
                    "id": "9876",
                    "name": "Survey Detail Test"
                },
                "submission": {
                    "udid": "detail-submission-id"
                }
            }
        """.trimIndent()

        LocalData.instant.strCheckingSurveyId = "9876"

        val latch = CountDownLatch(1)
        val success = booleanArrayOf(false)

        val httpCoreSlot = slot<JsonGetResult>()
        every { anyConstructed<HttpCore>().setCallBack(capture(httpCoreSlot)) } just Runs

        val api = PulseInsightsApi(mockContext, object : SurveyFlowResult {
            override fun result(strType: String, strExtend: String) {
                if (strType == Define.SURVEY_REQ_TYPE_PRESENT && strExtend == "sucess") {
                    success[0] = true
                }
                latch.countDown()
            }
        })

        api.getSurveyInformation()

        verify {
            anyConstructed<HttpCore>().composeRequestUrl(match { path ->
                path.contains("surveys/9876")
            }, any<Map<String, String>>())
        }

        verify { anyConstructed<HttpCore>().startRequest(any(), Define.SURVEY_REQ_TYPE_PRESENT) }

        httpCoreSlot.captured.getResult(mockResponse, Define.SURVEY_REQ_TYPE_PRESENT)

        val completed = latch.await(2, TimeUnit.SECONDS)

        assertTrue("Asynchronous operation should complete before timeout", completed)
        assertTrue("API call should be successful", success[0])

        assertEquals(
            "Survey ID should be correctly parsed",
            "9876",
            LocalData.instant.surveyPack.survey.id
        )
        assertEquals(
            "Submission ID should be correctly parsed",
            "detail-submission-id",
            LocalData.instant.strSubmitId
        )
    }

    @Test
    fun testGetSurveyInformation_WithEmptyId_ShouldReturnFalse() {
        LocalData.instant.strCheckingSurveyId = ""

        val latch = CountDownLatch(1)
        val success = booleanArrayOf(false)
        val receivedErrorSignal = booleanArrayOf(false)

        val httpCoreSlot = slot<JsonGetResult>()
        every { anyConstructed<HttpCore>().setCallBack(capture(httpCoreSlot)) } just Runs
        every { anyConstructed<HttpCore>().startRequest(any(), any()) } just Runs

        val api = PulseInsightsApi(mockContext, object : SurveyFlowResult {
            override fun result(strType: String, strExtend: String) {
                println("Received result: type=$strType, extend=$strExtend")
                if (strType == "error") {
                    receivedErrorSignal[0] = true
                } else if (strType == Define.SURVEY_REQ_TYPE_PRESENT && strExtend == "sucess") {
                    success[0] = true
                }
                latch.countDown()
            }
        })

        api.getSurveyInformation()

        httpCoreSlot.captured.getResult("", "error")
        val completed = latch.await(5, TimeUnit.SECONDS)

        assertTrue("Asynchronous operation should complete before timeout", completed)
        assertTrue("Should receive error signal", receivedErrorSignal[0])
        assertFalse("API call should fail", success[0])

        assertEquals(
            "Survey ID should remain empty",
            "",
            LocalData.instant.surveyPack.survey.id ?: ""
        )
        assertEquals("Submission ID should remain empty", "", LocalData.instant.strSubmitId)
    }

    //  MARK: - getQuestionDetail API
    @Test
    fun testGetQuestionDetail_WhenSuccessful_ShouldParseSurveyTickets() {
        val mockResponse = """
        [
            {
                "id": "123",
                "content": "How would you rate our service?",
                "question_type": "single_choice_question",
                "possible_answers": [
                    {
                        "id": "1",
                        "content": "Excellent"
                    },
                    {
                        "id": "2",
                        "content": "Good"
                    },
                    {
                        "id": "3",
                        "content": "Average"
                    }
                ]
            },
            {
                "id": "456",
                "content": "Any additional comments?",
                "question_type": "free_text_question"
            }
        ]
    """.trimIndent()

        val latch = CountDownLatch(1)
        val success = booleanArrayOf(false)

        val httpCoreSlot = slot<JsonGetResult>()
        every { anyConstructed<HttpCore>().setCallBack(capture(httpCoreSlot)) } just Runs

        val api = PulseInsightsApi(mockContext, object : SurveyFlowResult {
            override fun result(strType: String, strExtend: String) {
                if (strType == Define.SURVEY_REQ_TYPE_GETCONTENT && strExtend == "sucess") {
                    success[0] = true
                }
                latch.countDown()
            }
        })

        api.getQuestionDetail()

        verify {
            anyConstructed<HttpCore>().composeRequestUrl(match { path ->
                path.contains("surveys/${LocalData.instant.strCheckingSurveyId}/questions")
            }, any<Map<String, String>>())
        }

        verify { anyConstructed<HttpCore>().startRequest(any(), Define.SURVEY_REQ_TYPE_GETCONTENT) }

        httpCoreSlot.captured.getResult(mockResponse, Define.SURVEY_REQ_TYPE_GETCONTENT)

        val completed = latch.await(2, TimeUnit.SECONDS)

        assertTrue("Asynchronous operation should complete before timeout", completed)
        assertTrue("API call should be successful", success[0])

        assertEquals(
            "Survey tickets should be parsed correctly",
            2,
            LocalData.instant.surveyTickets.size
        )
        assertEquals(
            "First ticket ID should be '123'",
            "123",
            LocalData.instant.surveyTickets[0].id
        )
        assertEquals(
            "First ticket content should be 'How would you rate our service?'",
            "How would you rate our service?",
            LocalData.instant.surveyTickets[0].content
        )
    }

    @Test
    fun testGetQuestionDetail_WhenServerReturnsError_ShouldReturnFalse() {
        val latch = CountDownLatch(1)
        val success = booleanArrayOf(false)
        val receivedErrorSignal = booleanArrayOf(false)

        val httpCoreSlot = slot<JsonGetResult>()
        every { anyConstructed<HttpCore>().setCallBack(capture(httpCoreSlot)) } just Runs

        every { anyConstructed<HttpCore>().startRequest(any(), any()) } just Runs

        val api = PulseInsightsApi(mockContext, object : SurveyFlowResult {
            override fun result(strType: String, strExtend: String) {
                println("Received result: type=$strType, extend=$strExtend")
                if (strType == "error") {
                    receivedErrorSignal[0] = true
                } else if (strType == Define.SURVEY_REQ_TYPE_GETCONTENT && strExtend == "sucess") {
                    success[0] = true
                }
                latch.countDown()
            }
        })

        LocalData.instant.surveyTickets.clear()

        api.getQuestionDetail()

        httpCoreSlot.captured.getResult("", "error")

        val completed = latch.await(5, TimeUnit.SECONDS)

        assertTrue("Asynchronous operation should complete before timeout", completed)
        assertTrue("Should receive error signal", receivedErrorSignal[0])
        assertFalse("API call should fail", success[0])
        assertTrue("Survey tickets should be empty", LocalData.instant.surveyTickets.isEmpty())
    }

    // MARK: - postAnswers API
    @Test
    fun testPostAnswers_ShouldSendCorrectRequest() {
        val latch = CountDownLatch(1)
        val success = booleanArrayOf(false)

        val httpCoreSlot = slot<JsonGetResult>()
        every { anyConstructed<HttpCore>().setCallBack(capture(httpCoreSlot)) } just Runs

        LocalData.instant.strSubmitId = "test-submission-id"
        LocalData.instant.strUdid = "test-udid"
        LocalData.instant.strAccountId = "test-account"
        val questionId = "q123"
        val answerId = "a456"
        val questionType = "single_choice_question"

        val api = PulseInsightsApi(mockContext, object : SurveyFlowResult {
            override fun result(strType: String, strExtend: String) {
                println("SurveyFlowResult triggered with type: $strType, extend: $strExtend")
                if (strType == Define.SURVEY_REQ_TYPE_SENDANSWER && strExtend == "sucess") {
                    success[0] = true
                }
                latch.countDown()
            }
        })

        api.postAnswers(answerId, questionId, questionType, object : EventListener {
            override fun onEvent(result: Any?) {
                println("Event received: $result")
            }
        })

        verify {
            anyConstructed<HttpCore>().composeRequestUrl(match { path ->
                path.contains("submissions/test-submission-id/answer")
            }, withArg { params ->
                assertTrue("Should contain question_id", params.containsKey("question_id"))
                assertEquals("Should have correct question_id", questionId, params["question_id"])
                assertTrue("Should contain answer_id", params.containsKey("answer_id"))
                assertEquals("Should have correct answer_id", answerId, params["answer_id"])
                true
            })
        }

        verify { anyConstructed<HttpCore>().startRequest(any(), Define.SURVEY_REQ_TYPE_SENDANSWER) }

        httpCoreSlot.captured.getResult("{}", Define.SURVEY_REQ_TYPE_SENDANSWER)

        val completed = latch.await(5, TimeUnit.SECONDS)

        assertTrue("Asynchronous operation should complete before timeout", completed)
        assertTrue("API call should be successful", success[0])
    }

    // MARK: - postAllAtOnce API
    @Test
    fun testPostAllAtOnce_ShouldSendCorrectRequest() {
        val latch = CountDownLatch(1)
        val success = booleanArrayOf(false)

        val httpCoreSlot = slot<JsonGetResult>()
        every { anyConstructed<HttpCore>().setCallBack(capture(httpCoreSlot)) } just Runs

        LocalData.instant.strSubmitId = "test-submission-id"
        LocalData.instant.strAccountId = "test-account"

        val mockAnswers = com.pulseinsights.surveysdk.util.SurveyAnswers()
        mockAnswers.setAnswer("q123", "single_choice_question", "a456")
        mockAnswers.setAnswer("q789", "free_text_question", "This is free text input answer")

        val api = PulseInsightsApi(mockContext, object : SurveyFlowResult {
            override fun result(strType: String, strExtend: String) {
                println("SurveyFlowResult triggered with type: $strType, extend: $strExtend")
                if (strType == Define.SURVEY_REQ_TYPE_SENDANSWER && strExtend == "sucess") {
                    success[0] = true
                }
                latch.countDown()
            }
        })

        api.postAllAtOnce(mockAnswers, object : EventListener {
            override fun onEvent(result: Any?) {
                println("Event received: $result")
            }
        })

        verify {
            anyConstructed<HttpCore>().composeRequestUrl(match { path ->
                path.contains("submissions/test-submission-id/all_answers")
            }, withArg { params ->
                assertTrue("Should contain identifier", params.containsKey("identifier"))
                assertEquals("Should have correct identifier", "test-account", params["identifier"])
                assertTrue("Should contain answers", params.containsKey("answers"))
                val answersJson = params["answers"] ?: ""
                assertTrue("Answers JSON should contain q123", answersJson.contains("q123"))
                assertTrue("Answers JSON should contain a456", answersJson.contains("a456"))
                assertTrue("Answers JSON should contain q789", answersJson.contains("q789"))
                true
            })
        }

        verify { anyConstructed<HttpCore>().startRequest(any(), Define.SURVEY_REQ_TYPE_SENDANSWER) }

        httpCoreSlot.captured.getResult("{}", Define.SURVEY_REQ_TYPE_SENDANSWER)

        val completed = latch.await(5, TimeUnit.SECONDS)

        assertTrue("Asynchronous operation should complete before timeout", completed)
        assertTrue("API call should be successful", success[0])
    }

    // MARK: - viewedAt API
    @Test
    fun testViewedAt_ShouldSendCorrectRequest() {
        val latch = CountDownLatch(1)
        val success = booleanArrayOf(false)

        val httpCoreSlot = slot<JsonGetResult>()
        every { anyConstructed<HttpCore>().setCallBack(capture(httpCoreSlot)) } just Runs

        LocalData.instant.strSubmitId = "test-submission-id"
        LocalData.instant.strAccountId = "test-account"

        val api = PulseInsightsApi(mockContext, object : SurveyFlowResult {
            override fun result(strType: String, strExtend: String) {
                println("SurveyFlowResult triggered with type: $strType, extend: $strExtend")
                if (strType == Define.SURVEY_REQ_VIEWED_AT && strExtend == "sucess") {
                    success[0] = true
                }
                latch.countDown()
            }
        })

        api.viewedAt()

        verify {
            anyConstructed<HttpCore>().composeRequestUrl(match { path ->
                path.contains("submissions/test-submission-id/viewed_at")
            }, withArg { params ->
                assertTrue("Should contain identifier", params.containsKey("identifier"))
                assertEquals("Should have correct identifier", "test-account", params["identifier"])
                assertTrue("Should contain viewed_at timestamp", params.containsKey("viewed_at"))
                val viewedAt = params["viewed_at"] ?: ""
                assertTrue(
                    "viewed_at should be a valid timestamp",
                    viewedAt.matches(Regex("\\d{4}-\\d{2}-\\d{2}.*"))
                )
                true
            })
        }

        verify { anyConstructed<HttpCore>().startRequest(any(), Define.SURVEY_REQ_VIEWED_AT) }

        httpCoreSlot.captured.getResult("{}", Define.SURVEY_REQ_VIEWED_AT)

        val completed = latch.await(5, TimeUnit.SECONDS)

        assertTrue("Asynchronous operation should complete before timeout", completed)
        assertTrue("API call should be successful", success[0])
    }

    @Test
    fun testViewedAt_WhenServerReturnsError_ShouldReturnFalse() {
        val latch = CountDownLatch(1)
        val success = booleanArrayOf(false)
        val receivedErrorSignal = booleanArrayOf(false)

        val httpCoreSlot = slot<JsonGetResult>()
        every { anyConstructed<HttpCore>().setCallBack(capture(httpCoreSlot)) } just Runs

        LocalData.instant.strSubmitId = "test-submission-id"
        LocalData.instant.strAccountId = "test-account"

        val api = PulseInsightsApi(mockContext, object : SurveyFlowResult {
            override fun result(strType: String, strExtend: String) {
                println("SurveyFlowResult triggered with type: $strType, extend: $strExtend")
                if (strType == "error") {
                    receivedErrorSignal[0] = true
                } else if (strType == Define.SURVEY_REQ_VIEWED_AT && strExtend == "sucess") {
                    success[0] = true
                }
                latch.countDown()
            }
        })

        api.viewedAt()

        verify {
            anyConstructed<HttpCore>().composeRequestUrl(match { path ->
                path.contains("submissions/test-submission-id/viewed_at")
            }, any<Map<String, String>>())
        }

        verify { anyConstructed<HttpCore>().startRequest(any(), Define.SURVEY_REQ_VIEWED_AT) }

        httpCoreSlot.captured.getResult("", "-1")

        val completed = latch.await(5, TimeUnit.SECONDS)

        assertTrue("Asynchronous operation should complete before timeout", completed)
        assertTrue("Should receive error signal", receivedErrorSignal[0])
        assertFalse("API call should fail", success[0])
    }
}