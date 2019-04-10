x dw 0
y dw 0
tab   res    48            % Store results of operations
entry
allign
      addi r3,r0, 7
      sw x(r2), r3
      addi r3, r0, 13
      andi r3, r3, 9
      sw y(r2), r3


lw r1, y(r2)
jl r15,putint
hlt
