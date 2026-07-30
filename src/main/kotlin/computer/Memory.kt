package computer

import exception.MemoryOutOfBoundsException

abstract class Memory(
	protected val data : ByteArray
) : MemoryIO
{
	override fun read(address : UShort) : Byte
	{
		if (address.toInt() >= data.size)
			throw MemoryOutOfBoundsException("read.")
		return data[address.toInt()]
	}
	override fun write(address : UShort, value : Byte)
	{
		if (address.toInt() >= data.size)
			throw MemoryOutOfBoundsException("write.")

		data[address.toInt()] = value
	}

	open fun zero()
	{
		for (i in data.indices) data[i] = 0
	}
}

