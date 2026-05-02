import subprocess
import os

os.chdir(r'D:\Documents\GitHub\slimefun\InfinityExpansion')
subprocess.run(['git', 'status'])
subprocess.run(['git', 'add', '.'])
subprocess.run(['git', 'commit', '-m', 'Fix Slimefun5 API compatibility errors'])
subprocess.run(['git', 'push', 'origin', 'master'])
print("Pushed")
