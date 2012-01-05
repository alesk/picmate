% Radius estimation

% Input data
function [r, cp_x, cp_y] = radiusFun(x1, y1, x2, y2, x3, y3)


    r = -1;
    cp_x = NaN;
    cp_y = NaN;
    eps = 0.0001;

    % Plot it
    %dataX = [r_me, r_0, r_pe]';
    %plot (dataX(:, 1), dataX(:, 2), '*');

    % Compute centers
    c_ax = (x1 + x2)/2;
    c_ay = (y1 + y2)/2;
    c_bx = (x2 + x3)/2;
    c_by = (y2 + y3)/2;
    c_x = c_bx - c_ax;
    c_y = c_by - c_ay;

    % Compute directions
    nr_a = sqrt((x2-x1)^2 + (y2-y1)^2);
    nr_b = sqrt((x3-x2)^2 + (y3-y2)^2);

    if (abs(nr_a)>eps || abs(nr_b)>eps)
        s_ax = (y1 - y2)/nr_a;
        s_ay = (x2 - x1)/nr_a;
        s_bx = (y2 - y3)/nr_b;
        s_by = (x3 - x2)/nr_b;
    else
        return;
    end

    % Compute intersection and radus;
    detSys = s_ax*s_by - s_ay*s_bx;
    if (detSys > eps)
        u = (c_x*s_by - c_y*s_bx)/detSys;
        v = (c_x*s_ay - c_y*s_ax)/detSys;
        if ((u>0 && v>0) || (u<0 && v<0))
            cp_x = c_ax + u*s_ax;
            cp_y = c_ay + u*s_ay;
            r = sqrt((cp_x-x2)^2 + (cp_y-y2)^2);
        end
    end

end

