package computer.cpu.instruction

import computer.cpu.D5700CPUMemoryAccess

internal class DrawInstruction(cpuMemory : D5700CPUMemoryAccess) : D5700Instruction(cpuMemory)
{
	override fun performInstruction(parameters : Array<Int>)
	{
		val addr = (parameters[2] + parameters[1] * 8).toUShort()
		cpuMemory.writeToScreen(addr, cpuMemory.registers[parameters[0]])
	}
}

