#!/usr/bin/python
# vim: set fileencoding=utf-8 :

import string

def read_separated_data(lines):
    """measurements separated by NEW_MEASUREMENT lines"""
    measurements=[ map(string.strip, l.split(', ')) for l in lines]
    slices = map(lambda x:x[0],
        filter(lambda x:len(x[1])  == 1, enumerate(measurements)))
    ranges = zip(slices, slices[1:])
    data = [filter(lambda x: len(x) > 1, measurements[f:t])
            for f,t in ranges]
    return data

def read_data(lines):
    """ reads comma separated numbers """
    return [[ map(lambda x: float(string.strip(x)), l.split(', ')) for l in lines]]
