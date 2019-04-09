x dw 0
y dw 0
tab   res    48            % Store results of operations
entry
allign
      addi r3,r0, 7
      sw x(r2), r3
      sw y(r2), r3

% Perform each arithmetic operation with X and Y

     % add    r2,r3,r4
      %sb     tab(r1),r2
      %addi   r1,r0,0

lw r1, x(r2)
jl r15,putint
hlt