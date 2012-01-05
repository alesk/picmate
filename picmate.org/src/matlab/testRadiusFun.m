% Compose randomized circle
n = 14;
r0 = 2; 
eps=0.2;
partT = [0:n-1]+0.1*rand();
dataXY = [r0*cos((2*pi/n)*partT)-r0; r0*sin((2*pi/n)*partT)] + eps*rand(2,n); 
plot(dataXY(1,:), dataXY(2,:))
hold on;

radLst = zeros(1,n-2);
centersLst = zeros(2,n-2);
for (ii=2:n-1)
    [radLst(ii-1), cp_x, cp_y] = radiusFun(dataXY(1,ii-1), dataXY(2,ii-1), dataXY(1,ii), dataXY(2,ii), dataXY(1,ii+1), dataXY(2,ii+1));
    centersLst(1, ii-1) = cp_x;
    centersLst(2, ii-1) = cp_y;
end
plot(centersLst(1,:), centersLst(2,:),'--rs');

% Test line
dataXY=[1,2;2,4;3,6]';
rC1 = radiusFun(dataXY(1,1), dataXY(2,1), dataXY(1,2), dataXY(2,2), dataXY(1,3), dataXY(2,3));

% Test wrong orientation
dataXY=[0,0;1,1;2,0]';
rC2 = radiusFun(dataXY(1,1), dataXY(2,1), dataXY(1,2), dataXY(2,2), dataXY(1,3), dataXY(2,3));

% Test right orientation
dataXY=[0,1;1,0;2,1]';
rC3 = radiusFun(dataXY(1,1), dataXY(2,1), dataXY(1,2), dataXY(2,2), dataXY(1,3), dataXY(2,3));