
import component.D5700ComputerFacade
import java.io.File

fun main() {
    print("Enter filename: ")
    val filename = readln()

    var data: ByteArray
    try {
        data = File(filename).readBytes()
    } catch (e: Exception) {
        System.err.println("Failed to read file: ${e.message}")
        return
    }

    try {
        val cp = D5700ComputerFacade()
        cp.runProgram(data)
    }
    catch (t : Throwable)
    {
        t.printStackTrace()
    }
    catch (e: Exception)
    {
        System.err.println("D5700 ERROR: ${e.message}")
    }
}

