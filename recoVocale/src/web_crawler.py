import requests

def main():
    pronunciation_english_get("lyon")


def pronunciation_french_get(pronunciation_name,lan = "fra"):

    headers1 = {"user-agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko)                     Chrome/85.0.4183.102 Safari/537.36","referer": "https://fanyi.baidu.com/?aldtype=16047"}

    params1 = {"lan":lan,"text":pronunciation_name,"spd":"3","source":"web"}

    url = "https://fanyi.baidu.com/gettts"

    pronunciation_french_get = requests.get(url,headers = headers1,params = params1)

    with open("../data/{}.mp3".format(pronunciation_name),'wb') as file:
        file.write(pronunciation_french_get.content)

    file.close()




def pronunciation_english_get(pronunciation_name,lan = "en"):

    headers1 = {"user-agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko)                     Chrome/85.0.4183.102 Safari/537.36","referer": "https://fanyi.baidu.com/?aldtype=16047"}

    params1 = {"lan":lan,"text":pronunciation_name,"spd":"3","source":"web"}

    url = "https://fanyi.baidu.com/gettts"
    pronunciation_english_get = requests.get(url,headers = headers1,params = params1)
    with open("./data/english/{}.wav".format(pronunciation_name),'wb') as file:
        file.write(pronunciation_english_get.content)

    file.close()

if __name__ == "__main__":
    main()



