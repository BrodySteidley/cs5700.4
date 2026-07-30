package computer

import exception.MemoryAccessException

class D5700Screen : Memory(ByteArray(64))
{
	override fun read(address : UShort) : Byte
	{
		throw MemoryAccessException("Screen is write-only.")
	}

	override fun write(address : UShort, value : Byte)
	{
		super.write(address, value)
		updateDisplay()
	}

	private fun updateDisplay()
	{
		println()
		for (y in 0..7)
		{
			for (x in 0..7)
				print(data[x + y * 8].toInt().toChar())
			println()
		}
	}

	override fun zero()
	{
		for (i in data.indices) data[i] = 46
	}
}

