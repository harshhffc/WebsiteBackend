package com.homefirstindia.homefirstwebsite.utils

const val DEFAULT_ERROR_MESSAGE = "Something went wrong.Please try again!"
const val NA = "NA"
const val SUCCESS = "success"
const val FAILURE = "failure"
const val DELIVRD = "DELIVRD"
const val MESSAGE = "message"
const val ERROR = "error"
const val ACTION = "action"
const val STATUS = "status"
const val AUTHORIZATION = "Authorization"
const val SOURCE_PASSCODE = "sourcePasscode"

const val CAREERS_EMAIL = "careers@homefirstindia.com"
const val RANAN_RODRIGUES_EMAIL = "ranan.rodrigues@homefirstindia.com"
const val ANUJ_BHELKAR_EMAIL = "anuj.bhelkar@homefirstindia.com"
const val KUNDAN_SINGH_EMAIL = "kundan.singh@homefirstindia.com"


const val CATEGORY_NAME = "categoryName"
const val JOB_TYPE = "jobType"
const val FAQ_CATEGORY_NAME = "faqCategoryName"
const val JOB_LIST_ID = "jobListId"
const val KEYWORD = "keyword"
const val ID = "id"
const val SOURCE_PASSCODE_KEY = "RJGH*#T46@G4GET82BDTY%&(RU=}#TJK"
const val ENC_KEY ="SFKRuuurhCha68FtUvLmNSWuKxxSomyMrtHFYsn8HbnFrmTvS3clPg6DJn+na4GR"
const val HOME_LOAN = "home-loan"

const val captchaURL = "https://google.com/recaptcha/api/siteverify"
const val captchaSecretKey = "6LcRyp4pAAAAANXKleLQAz6GkT4eSSJys2hgfAGe"

const val THREAD_POOL_TASK_EXECUTOR = "threadPoolTaskExecutor"


enum class Errors(val value: String) {
    UNKNOWN("UNKNOWN"),
    FAILED("FAILED"),
    INVALID_CREDENTIALS("INVALID_CREDENTIALS"),
    RESOURCE_NOT_FOUND("RESOURCE_NOT_FOUND"),
    ACCESS_DENIED("ACCESS_DENIED"),
    UNAUTHORIZED_ACCESS("UNAUTHORIZED_ACCESS"),
    DUPLICATE_RECORD("DUPLICATE_RECORD"),
    STRING_TOO_LONG("STRING_TOO_LONG"),
    JSON_PARSER_ERROR("JSON_PARSER_ERROR"),
    OPERATION_FAILED("OPERATION_FAILED"),
    INVALID_DATA("INVALID_DATA"),
    INVALID_REQUEST("INVALID_REQUEST"),
    INCONLUSIVE("INCONCLUSIVE"),
    SERVICE_NOT_FOUND("SERVICE_NOT_FOUND");
}
enum class Actions(val value: String) {
    AUTHENTICATE_AGAIN("AUTHENTICATE_AGAIN"),
    RETRY("RETRY"),
    FIX_RETRY("FIX_RETRY"),
    CANCEL("CANCEL"),
    CONTACT_ADMIN("CONTACT_ADMIN"),
    DO_REGISTRATION("DO_REGISTRATION"),
    DO_VERIFICATION("DO_VERIFICATION"),
    GO_BACK("GO_BACK"),
    DO_LOGIN("DO_LOGIN"),
    DO_LOOKUP("DO_LOOKUP"),
    CONTINUE("CONTINUE");
}

enum class FileTypesExtentions(
    val ext: String,
    val contentType: String,
    val displayName: String
) {
    PDF(".pdf", "application/pdf", "PDF"),
    DOC(".doc", "application/msword", "Microsoft Word Document"),
    DOCX(".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "Microsoft Word Document");

    companion object {
        fun getFileTypeFromContentType(contentType: String): FileTypesExtentions? {
            for (item: FileTypesExtentions in entries) {
                if (item.contentType == contentType) return item
            }
            return null
        }

        fun getFileTypeFromExtension(extension: String): FileTypesExtentions? {
            for (item: FileTypesExtentions in entries) {
                if (item.ext.equals(extension, ignoreCase = true)) return item
            }
            return null
        }

        fun isValidExtension(extension: String): Boolean {
            return getFileTypeFromExtension(extension) != null
        }
    }
}


