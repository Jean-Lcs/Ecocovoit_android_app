import numpy as np
import scipy.io.wavfile
from scipy.fftpack import dct
import os


def MFCC(file,frame_time = 0.040,frame_overlap = 0.010, file_directory = 'data'):
    """
    This function calculates the MFCC (Mel-Frequency Cepstral Coefficients) of an audio signal formatted to .wav .
    There are six steps:
    1)Parsing the signal in small duration frames (lesser or equal to 40 ms : here default 40 ms) with some overlapping
    2)Application of Hamming window to reduce Fourier Transform ripples
    3)Calculating the Fourier Transform of each frame
    4)Taking the modulus of the FT i.e. the amplitude
    5)Transform frequency domain into mel (logarithmic) domain and take signal result through mel triangular filters
    6)Taking the logarithm (base 10) of the resulting signal    

    :param file: The filename of an audio .wav file
    :param frame_time: The duration of each frame (by default : 40ms)
    :param frame_overlap: The overlap duration (by default : 10 ms)
    :param file_directory: This parameter was added for testing facilities. Default directory is data.
    :return: A numpy array which is the MFCC of the signal
    """
    cur_path = os.path.dirname(__file__)
    new_path = os.path.relpath('../'+file_directory+'/'+file, cur_path)   #audio samples stored in data
    
    
    sample_rate, signal = scipy.io.wavfile.read(new_path)  # File assumed to be at the end of the new path
                                                         #For .wav files :  sample rate is 44.1 kHz

    if(signal.ndim == 2):    # i.e. the wav file has 2 channels: one for left speaker and one for right speaker
        signal_ord = [signal[i][1] for i in range(len(signal))] 
    
    else:
        signal_ord = signal
        
        
       
    sliced_signal = parse(signal_ord,sample_rate, frame_time,frame_overlap) #step 1
           
    for elem in sliced_signal:
        elem *= np.hamming(np.floor(sample_rate*frame_time)) #step 2
        
    print("parse check")

    fft_vector= np.fft.fft(sliced_signal)   #step 3
        
    print("fft check")

    fft_norm_vector = np.square(np.absolute(fft_vector)) #step 4
    
    print("fft_norm check")    
    
    mel_mapped_vect = np.array([ mel(fft_norm_vector[i], sample_rate) for i in range(len(fft_norm_vector))]) #step 5 
    
    print("mel mapping check")
    
    mel_mapped_vect = 20*np.log10(mel_mapped_vect) #step 6

    mfcc = np.array(dct(mel_mapped_vect))     
    
    return mfcc[0:12]   #Per convention, only the 13 first mfcc are taken into account



def parse(signal, sample_rate, frame_time, frame_overlap):
    
    """
    This function parses the audio signal in frames of smaller duration with some overlap.
    The overlap is used because the Hamming window makes unusable the border values of the frames, with overlapping they are not null in every frame.
    :param signal: The numpy array representing the signal, its len is duration * sample_rate
    :param sample_rate: The sample rate of the 
    :param frame_time: The duration of each frame
    :param frame_overlap: The overlap duration (by default : 10 ms)
    :return: A numpy array with one element being one frame of the signal
    """   
    frame_count= int(np.floor( frame_time *  sample_rate))     #number of samples in each frame
    overlap_count = int(np.floor(frame_overlap *sample_rate))   #number of samples in the overlap
    
    frame_overlay = frame_count - overlap_count
    
    frame_numbers = int(np.floor( (len(signal) -frame_count) / frame_overlay)) + 1 #len(signal) >= (frame_numbers - 1) * overlap_count + frame_count 
    
    return np.array([signal[i*frame_overlay:(i+1)*frame_overlay+overlap_count ] for i in range(frame_numbers)], dtype=np.float64) # each frame is still 1 frame wide
                                                                                                                
                                                                                                                

      
    
    
def mel(signal, sample_rate):
    
    """
    This function maps the signal's amplitude onto the mel scale, using triangular overlapping windows.
    :param signal: The numpy array representing the signal
    :param sample_rate: The sample rate of the signal
    :return: A numpy array with one element being one mel filter mapped amplitude
    """
    
    M= 2595 * np.log10(1 + sample_rate/1400)     # Nyquist frequency into mel non-linear scale:  sample_rate/2 * 1/700 in the formulae ie sample_rate/1400
    
    mel_freq= [int(np.floor((700 * (10**((M*i/41) / 2595) - 1)))) for i in range(42)] #On mel scale, we equally distribute the [0;M] range. It becomes logarithmic distribution on Fourier frequencies.
    
    #To get 40 triangles, it requires 42 mel frequencies

    log_mel = np.zeros(40,dtype=np.float64) #array initialisation
 
    fft_bin_freq  = [int(np.floor((len(signal)+1)*mel_freq[i]/sample_rate)) for i in range(42)]      
    #fft_bin is an approximation for frequencies after fft : dig into the theory
    

  

    for i in range(1,len(fft_bin_freq)-1):              #there is an index offset here: it's log_mel[i-1] because i begins from 1 (else fft_bin_freq[i-1] cannot exist)
        
        for k in range(fft_bin_freq[i-1]+1, fft_bin_freq[i]):
                        
            log_mel[i-1] += signal[k]* (k - fft_bin_freq[i-1])/(fft_bin_freq[i]-fft_bin_freq[i-1])

        for k in range(fft_bin_freq[i], fft_bin_freq[i+1]):
                                   
            log_mel[i-1] += signal[k]* (fft_bin_freq[i+1] - k)/(fft_bin_freq[i+1]-fft_bin_freq[i])
             
    
    return log_mel





