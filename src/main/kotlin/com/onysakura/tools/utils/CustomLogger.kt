package com.onysakura.tools.utils

import java.io.IOException
import java.nio.file.Paths
import java.util.*
import java.util.logging.*
import java.util.logging.Formatter

@Deprecated("useless")
class CustomLogger {

    companion object {
        private val CLASS_NAME_LENGTH_LIMIT = 30
        private val IS_SAVE_LOG_FILE = false
        private val LOG_FILE_LEVEL = Level.SEVERE
        private val LOG_CONSOLE_LEVEL = Level.INFO
        var fileHandler: FileHandler? = null
        var formatter: Formatter? = null

        init {
            formatter = object : Formatter() {
                override fun format(record: LogRecord): String {
                    return getColor(record.level) +
                            DateUtils.format(Date(record.millis), DateUtils.YYYY_MM_DD_HH_MM_SS).toString() + " " + "[" + String.format("%5s", getLevel(record.level)) + "] " + getShortClassName(record.loggerName).toString() + ": " + record.message.toString() + "\u001b[0m\n"
                }
            }
            if (IS_SAVE_LOG_FILE) {
                val path = Paths.get("logs")
                try {
                    fileHandler = FileHandler(path.toFile().absolutePath + "/" + DateUtils.format(Date(), DateUtils.YYYYMMDDHHMMSS) + ".log")
                    fileHandler!!.setFormatter(formatter)
                    fileHandler!!.setLevel(LOG_FILE_LEVEL)
                } catch (ignored: IOException) {
                }
            }
        }

        fun getLevel(level: Level): String {
            return when (level.getName()) {
                "SEVERE" -> "ERROR"
                "WARNING" -> "WARN"
                "INFO" -> "INFO"
                "FINE", "FINER", "FINEST" -> "DEBUG"
                else -> "DEBUG"
            }
        }

        fun getColor(level: Level): String {
            return when (level.getName()) {
                "SEVERE" -> "\u001B[31m"
                "WARNING" -> "\u001B[33m"
                "INFO" -> "\u001B[30m"
                "FINE", "FINER", "FINEST" -> "\u001B[37m"
                else -> "DEBUG"
            }
        }

        fun getLogger(loggerName: Class<*>): Log {
            val logger = Logger.getLogger(loggerName.name)
            logger.useParentHandlers = false
            logger.level = Level.ALL
            val consoleHandler = ConsoleHandler()
            consoleHandler.setFormatter(formatter)
            consoleHandler.level = LOG_CONSOLE_LEVEL
            logger.addHandler(consoleHandler)
            if (IS_SAVE_LOG_FILE && fileHandler != null) {
                logger.addHandler(fileHandler)
            }
            return Log(logger)
        }

        class Log(private val logger: Logger) {
            fun debug(msg: Any) {
                logger.fine(msg.toString())
            }

            fun info(msg: Any) {
                logger.info(msg.toString())
            }

            fun warn(msg: Any) {
                logger.warning(msg.toString())
            }

            fun error(msg: Any) {
                logger.severe(msg.toString())
            }
        }

        fun getShortClassName(className: String): String? {
            return if (className.length <= CLASS_NAME_LENGTH_LIMIT) {
                String.format("%" + CLASS_NAME_LENGTH_LIMIT + "s", className)
            } else String.format("%" + CLASS_NAME_LENGTH_LIMIT + "s", className.substring(className.lastIndexOf(".") + 1))
        }
    }
}