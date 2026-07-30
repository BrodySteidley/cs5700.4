package computer.cpu

import computer.MemoryIO

/* A class to represent registers and memory access inside the CPU.
 * it is intended to be internal to the cpu and its instructions,
 * but kotlin doesn't seem to have package level privacy */
internal class D5700CPUMemoryAccess(
	private val ROMIO : MemoryIO,
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

	fun readROM(a : UShort) : Byte = ROMIO.read(a)

	fun writeToScreen(address : UShort, value : Byte) = screenIO.write(address, value)

	fun readD7500Input() : Byte = inputIO.read(0.toUShort())
}

