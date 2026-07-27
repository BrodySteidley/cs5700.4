package component
interface MemoryIO
{
	fun read(address : UShort) : Byte
	fun write(address : UShort, value : Byte)
}

