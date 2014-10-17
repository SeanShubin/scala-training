package com.seanshubin.scala.training.core.file_system

import java.io._

import com.seanshubin.scala.training.core.ResourceDeallocation.ensureClose

import scala.annotation.tailrec

class FileSystemImpl(charsetName: String) extends FileSystem {
  def writeLines(fileDirectoryName: String, fileName: String, lines: Iterable[String]) {
    val filePath = fileDirectoryName + "/" + fileName
    val fileDirectory = new File(fileDirectoryName)
    fileDirectory.mkdirs()
    ensureClose(new PrintWriter(new OutputStreamWriter(new FileOutputStream(filePath), charsetName))) {
      out =>
        lines.foreach(out.println)
    }
  }

  def linesFromFileNamed(fileDirectoryName: String, fileName: String): Iterable[String] = {
    val filePath = fileDirectoryName + "/" + fileName
    ensureClose(new BufferedReader(new InputStreamReader(new FileInputStream(filePath), charsetName))) {
      in =>
        readRemainingLines(in, Nil)
    }
  }

  @tailrec
  private def readRemainingLines(in: BufferedReader, reversed: List[String]): List[String] = {
    val line = in.readLine()
    if (line == null) reversed.reverse
    else readRemainingLines(in, line :: reversed)
  }
}
