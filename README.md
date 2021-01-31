# JASM compiler (Assembler compiler in Java) by Bohdan Sokolovskyi

![log](https://github.com/bohdan-sokolovskyi/JASM-assembler-compiler/blob/master/src/assets/logo.png)

## Description
* Text editor and assembler compiler in java
* Compiler v 3.0 beta
* Simplified version of assembler compiler (intel x32) and text editor with color themes

## Demonstration
* IDE:
![demo 1](https://github.com/bohdan-sokolovskyi/JASM-assembler-compiler/blob/master/src/assets/compiler-demo-1.png)
![demo 2](https://github.com/bohdan-sokolovskyi/JASM-assembler-compiler/blob/master/src/assets/compiler-demo-2.png)
* Input:
```asm
data segment
    valdb1 db 11b
    valdb2 db 24
    valdb3 db 3h
    valdb4 db 120

    valdw1 dw 123*(24+3)
    valdw2 dw 110011111001b
    valdw3 dw 0cf9h
    valdw4 dw -345

    valdd1 dd (23+7)*(11+4)
    valdd2 dd 111000010b
    valdd3 dd 01c2h
    valdd4 dd -1024
data ends

code segment 
start:

    cli ; complete

    inc eax ; complete
    inc al ; complete

    dec dword ptr [edx + esi + 6]

    add bl, bh
    add ebx, ebx
    
    add ah, bh
                            
    valume dd -43

    or al, [edx + Esi + 6]
    or ecx, [ebx + eCx + 12]

    and [edx + eSi + 6], ah
    and [ebx + ecx + 12], eax

    mov al, 15
    mov bh, -105
    mov eax, 12h
    mov ecx, -155
    mov ebx, 11110011b
    mov ebx, 243

    cmp dword Ptr ds:[edx + esi + 3], 11110011b
    cmp dword ptr ss:[esi + eax + 10], 01c2h

    jb ljb

    ljb:

    
    jmp ljmp

    ljmp:

    jmp [eax + esi + 4]
    jmp ss:[ebp + esi + 2]

code ends 
end

```

* Output:
```
          JASM Java Assembly Compiler v.0.3 beta        Wed Jun 05 08:48:26 MSK 2019
            by Sokolovskyi Bohdan FAM KPI KV-73

0000   data segment
0000 03     valdb1 db 11b
0001 18     valdb2 db 24
0002 03     valdb3 db 3h
0003 78     valdb4 db 120
    
0004 0CF9     valdw1 dw 123*(24+3)
0006 0CF9     valdw2 dw 110011111001b
0008 0CF9     valdw3 dw 0cf9h
000A FEA7     valdw4 dw -345
    
000C 000001C2     valdd1 dd (23+7)*(11+4)
0010 C2010000     valdd2 dd 111000010b
0014 C2010000     valdd3 dd 01c2h
0018 00FCFFFF     valdd4 dd -1024
001C   data ends
    
    
0000   code segment 
0000  start:
    
0000 FA     cli ; complete
    
0001 40     inc eax ; complite
0002 FE C0     inc al ; complite
    
0004 FF 4C32 06     dec dword ptr [edx + esi + 6]
    
0008 02 DF     add bl, bh
000A 03 DB     add ebx, ebx
        
000C 02 E7     add ah, bh
                                
000E D5FFFFFF     valume dd -43
    
0012 0A 4432 06     or al, [edx + Esi + 6]
0016 0B 4C0B 0C     or ecx, [ebx + eCx + 12]
    
001A 20 6432 06     and [edx + eSi + 6], ah
001E 21 440B 0C     and [ebx + ecx + 12], eax
    
0022 B0 0F     mov al, 15
0024 B7 97     mov bh, -105
0026 B8 00000012     mov eax, 12h
002B B9 FFFFFF65     mov ecx, -155
0030 BB 000000F3     mov ebx, 11110011b
0035 BB 000000F3     mov ebx, 243
    
003A 81 7C32 03 000000F3     cmp dword Ptr ds:[edx + esi + 3], 11110011b
0042 36: 81 7C06 0A 000001C2     cmp dword ptr ss:[esi + eax + 10], 01c2h
    
004B OF 82 00000051 R     jb ljb
    
0051      ljb:
    
        
0051 EB 03 90 90 90     jmp ljmp
    
0056      ljmp:
    
0056 FF 6430 04     jmp [eax + esi + 4]
005A FF 6435 02     jmp ss:[ebp + esi + 2]
    
005E   code ends 
     end




            N a m e               Size    Length

DATA . . . . . . . . . . . . . . . .  32 Bit  001C
CODE . . . . . . . . . . . . . . . .  32 Bit  005E




            N a m e             Type     Value   Attr

START  . . . . . . . . . . . . . .  L NEAR   0000    CODE
LJB    . . . . . . . . . . . . . .  L NEAR   0051    CODE
LJMP   . . . . . . . . . . . . . .  L NEAR   0056    CODE


VALDB1 . . . . . . . . . . . . . .  L BYTE   0000    DATA
VALDB2 . . . . . . . . . . . . . .  L BYTE   0001    DATA
VALDB3 . . . . . . . . . . . . . .  L BYTE   0002    DATA
VALDB4 . . . . . . . . . . . . . .  L BYTE   0003    DATA
VALDW1 . . . . . . . . . . . . . .  L WORD   0004    DATA
VALDW2 . . . . . . . . . . . . . .  L WORD   0006    DATA
VALDW3 . . . . . . . . . . . . . .  L WORD   0008    DATA
VALDW4 . . . . . . . . . . . . . .  L WORD   000A    DATA
VALDD1 . . . . . . . . . . . . . .  L DWORD  000C    DATA
VALDD2 . . . . . . . . . . . . . .  L DWORD  0010    DATA
VALDD3 . . . . . . . . . . . . . .  L DWORD  0014    DATA
VALDD4 . . . . . . . . . . . . . .  L DWORD  0018    DATA
VALUME . . . . . . . . . . . . . .  L DWORD  000E    CODE


         0 Errors
```
