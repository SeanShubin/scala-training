package com.seanshubin.scala.training.web.exception_info

case class ExceptionInfo(message: String, maybeCause: Option[ExceptionInfo], stackTrace: Seq[StackTraceElementInfo]) {
  def toMultipleLineString: Seq[String] = {
    def stackTraceElementToString(stackTraceElementInfo: StackTraceElementInfo): String = {
      val StackTraceElementInfo(declaringClass, methodName, fileName, lineNumber) = stackTraceElementInfo
      s"    $declaringClass.$methodName($fileName:$lineNumber)"
    }
    def toMultipleLineString(maybeExceptionInfo: Option[ExceptionInfo]): Seq[String] = {
      maybeExceptionInfo match {
        case Some(ExceptionInfo(message, maybeCause, stackTrace)) =>
          Seq(message) ++ stackTrace.map(stackTraceElementToString) ++ toMultipleLineString(maybeCause)
        case None => Seq()
      }
    }
    toMultipleLineString(Some(this))
  }
}

object ExceptionInfo {
  def fromException(exception: Throwable): ExceptionInfo = {
    fromExceptionOrNull(exception).get
  }

  def fromExceptionOrNull(exception: Throwable): Option[ExceptionInfo] = {
    if (exception == null) None
    else {
      val stackTrace: Seq[StackTraceElementInfo] = exception.getStackTrace.map(StackTraceElementInfo.fromStackTraceElement)
      val message: String = s"${exception.getClass.getName} ${exception.getMessage}"
      val maybeCause: Option[ExceptionInfo] = fromExceptionOrNull(exception.getCause)
      val exceptionInfo: ExceptionInfo = ExceptionInfo(message, maybeCause, stackTrace)
      val result: Option[ExceptionInfo] = Some(exceptionInfo)
      result
    }
  }
}
