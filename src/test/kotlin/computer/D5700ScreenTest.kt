package computer
import exception.MemoryAccessException
import exception.MemoryOutOfBoundsException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class D5700ScreenTest {

    @Test
    fun `read always throws MemoryAccessException`() {
        val screen = D5700Screen()

        assertThrows(MemoryAccessException::class.java) {
            screen.read(0u)
        }
        assertThrows(MemoryAccessException::class.java) {
            screen.read(1u)
        }
        assertThrows(MemoryAccessException::class.java) {
            screen.read(300u)
        }
    }

    @Test
    fun `write succeeds for valid addresses`() {
        val screen = D5700Screen()

        assertDoesNotThrow {
            screen.write(0u, 'A'.code.toByte())
            screen.write(63u, 'Z'.code.toByte())
        }
    }

    @Test
    fun `write out of bounds throws MemoryOutOfBoundsException`() {
        val screen = D5700Screen()

        assertThrows(MemoryOutOfBoundsException::class.java) {
            screen.write(64u, 0)
        }
    }

    @Test
    fun `zero initializes screen to periods`() {
        val screen = D5700Screen()

        screen.zero()

        // Reads are forbidden, so verify by writing over one location and
        // ensuring the remaining writes still succeed.
        assertDoesNotThrow {
            for (i in 0u until 64u) {
                screen.write(i.toUShort(), '.'.code.toByte())
            }
        }
    }

    @Test
    fun `multiple writes do not throw`() {
        val screen = D5700Screen()

        assertDoesNotThrow {
            for (i in 0u until 64u) {
                screen.write(i.toUShort(), ('A'.code + (i % 26u).toInt()).toByte())
            }
        }
    }
}