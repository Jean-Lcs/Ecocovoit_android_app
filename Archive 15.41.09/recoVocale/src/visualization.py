
import MFCC_overlap as mfcc_overlap
import DTW  as dtw
import numpy as np
import matplotlib.pyplot as plt
from matplotlib import cm


def print_MFCC(file):
    """
    This function prints into matplotlib the graph of audio file's mfcc.
    
    :param file: The filename of the audio formatted to .wav
    """
    mfcc_array = mfcc_overlap.MFCC(file)
    fig, ax = plt.subplots()
    mfcc_array= np.swapaxes(mfcc_array, 0 ,1)
    cax = ax.imshow(mfcc_array, interpolation='nearest', cmap=cm.coolwarm, origin='lower')
    ax.set_title('MFCC')

    plt.show()
    
    


print_MFCC("paris.wav")
