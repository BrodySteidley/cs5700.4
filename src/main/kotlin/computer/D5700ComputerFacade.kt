package computer

import computer.cpu.D5700CPU

class D5700ComputerFacade()
{
	private var rom : ROM = ROM(ByteArray(0))
	private val ram = D5700RAM()
	private val screen = D5700Screen()
	private val input = D5700Input()
	private var cpu : D5700CPU = D5700CPU(rom, ram, screen, input)

	fun runProgram(bytecode : ByteArray)
	{
		rom = ROM(bytecode)
		cpu = D5700CPU(rom, ram, screen, input)

		ram.zero()
		screen.zero()

		cpu.start()
	}

}

