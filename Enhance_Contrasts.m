I = imread('290418_cg38_0R1L_InsCre_GCaMP_islet.tif');
J = adapthisteq(I);%CLAHE enforcing contrasts 
imshowpair(I,J,'montage');
title('Original Image (left) and Contrast Enhanced Image (right)');

