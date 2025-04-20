# 读取合并后的文件
$combinedFile = Get-Content -Path ".\speed-score2.jpg" -Encoding Byte

# 拆分出第一个文件
$file1Content = $combinedFile[0..(57837 - 1)]
$file1Content | Set-Content -Path ".\restored-speed-score.jpg" -Encoding Byte

# 拆分出第二个文件
$file2Content = $combinedFile[57837..($combinedFile.Length - 1)]
$file2Content | Set-Content -Path ".\restored-Ethernet-n34rw09w.zip" -Encoding Byte
