import sys
import subprocess, time

engine = subprocess.Popen(
    '../engine/stockfish',
    universal_newlines=True,
    stdin=subprocess.PIPE,
    stdout=subprocess.PIPE,
)

def put(command):
    engine.stdin.write(command+'\n')

def get():
    # using the 'isready' command (engine has to answer 'readyok')
    # to indicate current last line of stdout
    movesToMate = ''
    engine.stdin.write('isready\n')
    while True:
        text = engine.stdout.readline().strip()
        if text == 'readyok':
            break
        elif text != '' and text.startswith('info depth'):
            movesToMate = text.split('1 pv ')[1]
    return movesToMate

def send_command(fen):
    put('position fen ' + fen)
    put('go movetime 500')
    time.sleep(0.6)

if __name__ == "__main__":
	with open('./MateIn4.pgn') as f:
		content = f.readlines()
	
	f = open('dataset','w')
	for line in content:
        # 8/6pk/8/1R5p/3K3P/8/6r1/8 b - - 0 42
		if(line.startswith('[FEN')):
			fen = line.split('"')[1]
			fen = fen.strip()
			send_command(fen)
			data = get()
			string = fen + " : " + data + "\n"
			print string
			f.write(string)
	
	f.close();
	put('quit')
