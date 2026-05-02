import os
import subprocess

os.chdir(r'D:\Documents\GitHub\slimefun\InfinityExpansion')
subprocess.run(['git', 'checkout', '.'])
subprocess.run(['python', 'final_fix.py'])
print("Done")
