from PIL import Image
import numpy as np

def image_to_matrix(image_path):
    # Open image file
    with Image.open(image_path) as img:
        # Convert image to grayscale
        img_gray = img.convert('L')

        # Convert image data to a numpy array
        img_data = np.asarray(img_gray)

        # Define a threshold for black/white. This may need adjusting.
        threshold = 128

        # Create a matrix of 0's and 1's based on the threshold
        binary_matrix = np.where(img_data < threshold, 1, 0)

    return binary_matrix

# Ask the user for the image path
image_path = input("Please enter the image path: ")

result = image_to_matrix(image_path)

# Print the result to see the binary matrix
for row in result:
    print(''.join(str(i) for i in row))
