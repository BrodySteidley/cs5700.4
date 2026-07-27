package exception;

open class MemoryOutOfBoundsException(s : String) : MemoryAccessException("Out of bounds $s")
