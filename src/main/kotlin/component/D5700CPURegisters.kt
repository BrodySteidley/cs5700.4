package component

class D5700CPURegisters(
    val ROMIO : MemoryIO,
    private val RAMIO : MemoryIO,
    private val screenIO : MemoryIO,
    private val inputIO : MemoryIO,
)
{
	val registers = ByteArray(8)
	var programCounter : Short = 0
	var timer : Byte = 0
	var address : Short = 0
	var memory : Boolean = false

	fun writeSelectedMemory(value : Byte)
	{
		if (memory)
			ROMIO.write(address.toUShort(), value)
		else
			RAMIO.write(address.toUShort(), value)
			
	}

	fun readSelectedMemory() : Byte = if (memory) ROMIO.read(address.toUShort()) else RAMIO.read(address.toUShort())

	fun writeToScreen(address : UShort, value : Byte) = screenIO.write(address, value)

	fun readD7500Input() : Byte = inputIO.read(0.toUShort())
}

