import java.nio.charset.CodingErrorAction

import Models.{Seconds, Transcript}
import com.google.gson.Gson

import scala.io.{Codec, Source}

/**
  *
  * @author Timothy Bent on 11/10/2018
  *
  */
object Main extends App {


  override def main ( args : Array[String]): Unit ={
    val paths = List[String]("C:\\Users\\tim-b\\Downloads\\json%2Frecording 1 implementation.json"
      , "C:\\Users\\tim-b\\Downloads\\json%2FRecorder 2 functionality.json"
      , "C:\\Users\\tim-b\\Downloads\\json%2Frecorder 3 Timer implement.json")

    var startTime = System.currentTimeMillis()
    paths.foreach( p => decentralizeWordFromFile(p))
    var endTime = System.currentTimeMillis()
    println(endTime - startTime)
  }

  def decentralizeWordFromFile( fileName : String){
    var input = Source.fromFile(fileName)(Codec.UTF8.decoder.onMalformedInput(CodingErrorAction.IGNORE))
    var json = input mkString
    var gson = new Gson()
    val transcript = gson.fromJson(json, classOf[Transcript])
    println(s"This is the transcript for ${fileName.substring(fileName.lastIndexOf("\\") + 1)}")
    transcript.results foreach (r => {
      println()
      r.alternatives foreach {
        a =>
          println(s"Confidence: ${a.confidence} Transcript ${a.transcript}")
          a.words foreach (w => println(s" Word: ${w.word} | Start Time ${convertTTValue(w.startTime.seconds) * 1000 + (w.startTime.nanos / 1000000)}ms | End Time ${convertTTValue(w.endTime.seconds) * 1000 + (w.endTime.nanos / 1000000)}ms"))
      }
    })
  }

  def convertTTValue( seconds : Seconds): Int = { seconds.low + seconds.high >>> 1 }

}


/*public ArrayList<Words> getWordStream() {

        ArrayList<Words> wordList = new ArrayList<>();
        transcript.results.forEach((Results r) -> r.alternatives.forEach((Alternatives a) -> a.words.stream().map(w -> new Words(w.startTime, w.endTime, w.word)).forEach(wordList::add)));
        return null;
    }

    public ArrayList<Words> getWordStreams() {
        ArrayList<Words> wordList2 = new ArrayList<>();
        transcript.results.stream()
                .map(r -> r.alternatives.stream().map(a -> a.words.stream().map(w -> wordList2.add(new Words(w.startTime, w.endTime, w.word)))));
        return wordList2;
    }*/
