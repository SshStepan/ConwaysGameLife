from PIL import Image

frames = []

for frame_number in range(1, 1000):
    frame = Image.open(f'snaps/{frame_number}.jpg')
    frames.append(frame)

frames[0].save(
    'out.gif',
    save_all=True,
    append_images=frames[1:],
    optimize=True,
    duration=30,
    loop=0
)