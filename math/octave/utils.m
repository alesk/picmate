1; % empty command to prevent treating this file as function file

function plotme(time, s1, s2, s1_label, s2_label)
	subplot(3,2,1);
	plot(time, s1(:,1));
	title(strcat(s1_label," X"));
	
	subplot(3,2,3);
	plot(time, s1(:,2));
	title(strcat(s1_label," y"));
	
	subplot(3,2,5);
	plot(time, s1(:,3));
	title(strcat(s1_label," Z"));
	
	subplot(3,2,2);
	plot(time, s2(:,1));
	title(strcat(s2_label," X"));
	
	subplot(3,2,4);
	plot(time, s2(:,2));
	title(strcat(s2_label," Y"));
	
	subplot(3,2,6);
	plot(time, s2(:,3));
	title(strcat(s2_label," Z"));
	
	print(strcat(s1_label, "_",s2_label,".png"));
endfunction

% vector magnitude
function ret = mag(x,y,z)
	ret = sqrt(x*x + y*y + z*z);
endfunction
