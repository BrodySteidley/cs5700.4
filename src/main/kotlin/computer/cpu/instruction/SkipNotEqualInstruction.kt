package computer.cpu.instruction

import computer.cpu.D5700CPURegisterAccess

internal class SkipNotEqualInstruction(cpuMemory : D5700CPURegisterAccess) : SkipEqualInstruction(cpuMemory)
{
	override fun performInstruction(parameters : Array<Int>)
	{
		skip = cpuMemory.registers[parameters[0]] != cpuMemory.registers[parameters[1]]
	}
}

