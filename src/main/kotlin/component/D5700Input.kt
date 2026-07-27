package component

import exception.MemoryAccessException

class D5700Input() : MemoryIO
{
	override fun read(address : UShort) : Byte
	{
		var value = 0
		while (true) {
			print("Enter a hexadecimal value (0-FF): ")
			val input = readln().trim().uppercase()
					/* will pause the instruction thread, but not the timer thread.
					* based on the specification, it is not clear to me whether or not
					* the timer is supposed to pause when getting input, I have interpreted it as not */

			if (input.matches(Regex("^[0-9A-F]{1,2}$"))) {
				value = input.toInt(16)
				break
			} else
				println("Invalid input. 1 or 2 hexadecimal digits (00 to FF).")
		}
		return value.toByte()
	}

	override fun write(address : UShort, value : Byte)
	{
		throw MemoryAccessException("D5700Input is not writable")
	}
}

