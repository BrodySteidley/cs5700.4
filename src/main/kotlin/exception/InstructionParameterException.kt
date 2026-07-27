package exception;

open class InstructionParameterException(instructionName : String) : InstructionException("Invalid parameters for $instructionName")
