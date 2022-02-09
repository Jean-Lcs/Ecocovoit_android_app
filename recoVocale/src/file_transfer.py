from pydub import AudioSegment
import wave





def trans_mp3_to_wav(filepath):
    song = AudioSegment.from_mp3(filepath)
    file = filepath.rstrip('.mp3')
    song.export("%s.wav"%file, format="wav")


"""
def trans_mp3_to_wav():

    sound = AudioSegment.from_file(filepath, format = 'MP3')

    f = wave.open("paris.wav", 'wb')
    f.setnchannels(1)
    f.setsampwidth(2)
    f.setframerate(16000)
    f.setnframes(len(sound._data))
    f.writeframes(sound._data)
    f.close()
"""

def main():

    trans_mp3_to_wav("../data/Calais.mp3")

if __name__ == "__main__":
    main()