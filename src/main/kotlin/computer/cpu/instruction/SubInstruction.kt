package computer.cpu.instruction

import computer.cpu.D5700CPURegisterAccess

internal class SubInstruction(cpuMemory : D5700CPURegisterAccess) : D5700Instruction(cpuMemory)
{
	override fun performInstruction(parameters : Array<Int>)
	{
		cpuMemory.registers[parameters[2]] = (cpuMemory.registers[parameters[0]] - cpuMemory.registers[parameters[1]]).toByte()
	}
}

