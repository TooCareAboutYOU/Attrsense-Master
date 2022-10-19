import numpy as np
a = np.fromfile("bias1_i.bin", dtype=np.int32).astype(np.float32)
a.tofile("bias1.bin")
a = np.fromfile("bias2_i.bin", dtype=np.int32).astype(np.float32)
a.tofile("bias2.bin")
a = np.fromfile("bias3_i.bin", dtype=np.int32).astype(np.float32)
a.tofile("bias3.bin")
