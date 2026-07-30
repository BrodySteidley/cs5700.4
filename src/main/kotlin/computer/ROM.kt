package computer

import exception.MemoryAccessException

class ROM(data : ByteArray) : Memory(data)
{
	override fun write(address : UShort, value : Byte)
	{
		throw MemoryAccessException("ROM is read only.")
	}
}

