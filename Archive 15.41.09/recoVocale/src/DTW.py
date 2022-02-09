#DTW algorithm

import numpy as np
from math import sqrt

def DTW(s1, s2):
    """
    This function is a DTW algorithm (Dynamic Time Warping) between two signals of possibly different lengths.
    The DTW algorithm calculates the 'cost' it takes to make s1 and s2 similar using a non linear overlay as principle.
    The more low is the result, the more s1 and s2 are similar.    

    :param s1: The signal 1
    :param s2: The signal 2    
    :return: The cost of the overlay normalized by the path length
    """
    n, m = len(s1), len(s2)
    
    pred = np.zeros((n,m), dtype='2i')    #Array to remember path
    dtw_matrix = np.zeros((n, m))    
   
    dtw_matrix[0, 0] = distance(s1[0], s2[0])
       
    
    #Case: j=0  predecessor can only be one line below
    for i in range(n):
        dtw_matrix[i,0] = dtw_matrix[i-1,0] + distance(s1[i],s2[0]) 
        pred[i,0]= (i-1,0)
    
    
    #Case: i=0 predecessor can only be one column on the left
    for j in range(1,m):        
        dtw_matrix[0,j] = dtw_matrix[0,j-1] + distance(s1[0],s2[j])
        pred[0,j] = (0,j-1)               
    
    
    for i in range(1,n):
        for j in range(1,m):
            dtw_matrix[i, j] = np.inf
                        
                        
    for i in range(1,n):
        for j in range(1,m):
            dist = distance(s1[i], s2[j])                 
            
            last_min = min_box(pred, dtw_matrix,i,j)
            dtw_matrix[i, j] = dist + last_min
    
    return dtw_matrix[n-1,m-1]/path_length(pred,n-1,m-1)
    
    

def distance(v1, v2):
    """
    This function calculates the Euclidean distance between two vectors of same length.
    It verifies vectors have same length before calculating the distance.
    
    :param v1: The vector 1
    :param v2: The vector 2    
    :return: The distance between v1 and v2
    """
    assert len(v1) == len(v2)      #Not supposed to happen

    return np.linalg.norm(v1-v2)




def min_box(pred, dtw_matrix, i, j):
    """
    This function picks the minimum from the square box formed by the point (i,j) and above, right and diagonal.
    It then stores the (i,j)coordinates into an array in the box of the min by side effect.
    
    :param pred: The array which stores predecessors
    :param dtw_matrix: The matrix with the values   
    :param i: The abscissa of the point    
    :param j: The ordinate of the point    
    """
    v1 =dtw_matrix[i-1,j]
    v2=dtw_matrix[i, j-1]
    v3= dtw_matrix[i-1, j-1]
    
    
    if v1 < v2:
        if v1 > v3:
            pred[i,j] = (i-1,j-1)
            return v3            
        else:           
            pred[i,j] = (i-1,j)
            return v1
    
    else:
        if v2 > v3:
            pred[i,j] = (i-1,j-1)
            return v3
            
        else:          
            pred[i,j] = (i,j-1)
            return v2
        
        
        

def path_length(pred,n,m):
    """
    This function calculates the length of a path. It recursively goes through the predessors till reaching coordinates (0,0).
        
    :param pred: The array which stores predecessors
    :param n: The abscissa of the point in the bottom right corner of the dtw matrix
    :param m: The ordinate of the point in the bottom right corner of the dtw matrix
    :return: The path's length
    """
    length = 0
    v=(n,m)
    
    while v[0] != 0 or v[1]!= 0:
        length += 1
        v = pred[v[0],v[1]]
    
    return length
        
        
        

