import os
import csv
import easygui

def telo(folder_name, i, starting_num):
    list_of_files = os.listdir(folder_name)
    # remove additional system files
    new_list_of_files = []
    for file in list_of_files:
        if file.endswith(".csv"):
            new_list_of_files.append(file)
    if i < len(starting_num) - 1:
        new_list_of_files = new_list_of_files[starting_num[i]:starting_num[i+1]]
        num_of_files = len(new_list_of_files)
    else:
        new_list_of_files = new_list_of_files[starting_num[i]::]
        num_of_files = len(new_list_of_files)
    foci = []
    exclusions = []
    for j in range(0, num_of_files):
        foci.append([])
        exclusions.append([])
    total_foci = 0
    total_nuclei = 0

    image_num = 0

    for filename in new_list_of_files:
        f = open(os.path.join(folder_name, filename),"r")
        csv_reader = csv.reader(f, delimiter = '\t')
        next(csv_reader)
        nuclei_num = 0
        # exclude cells with <20 telomeres

        for row in csv_reader:
            value = int(float(row[0].split(",")[5])/255)

            if value < 20:
                exclusions[image_num].append(nuclei_num)

            if value >= 20:
                foci[image_num].append(value)
                total_foci += value
                total_nuclei += 1

            nuclei_num += 1
        image_num += 1
        f.close()
    return foci, num_of_files, total_foci, total_nuclei, exclusions

def foci(folder_name, exclusions, i, starting_num):
    list_of_files = os.listdir(folder_name)
    # remove additional system files
    new_list_of_files = []
    for file in list_of_files:
        if file.endswith(".csv"):
            new_list_of_files.append(file)

    if i < len(starting_num) - 1:
        new_list_of_files = new_list_of_files[starting_num[i]:starting_num[i+1]]
        num_of_files = len(new_list_of_files)
    else:
        new_list_of_files = new_list_of_files[starting_num[i]::]
        num_of_files = len(new_list_of_files)

    foci = []
    for j in range(0, num_of_files):
        foci.append([])
    total_foci = 0
    total_nuclei = 0
    a = 0 #[0, 5)
    b = 0 #[5, 10)
    c = 0 #[10, 15)
    d = 0 #[15, 20)
    e = 0 #[20, inf)

    image_num = 0
    if exclusions == []:
        for filename in new_list_of_files:
            f = open(os.path.join(folder_name, filename),"r")
            csv_reader = csv.reader(f, delimiter = '\t')
            next(csv_reader)
            nuclei_num = 0

            for row in csv_reader:
                value = int(float(row[0].split(",")[5])/255)

                foci[image_num].append(value)
                total_foci += value
                total_nuclei += 1
                if value < 5:
                    a += 1
                if value >= 5 and value < 10:
                    b += 1
                if value >= 10 and value < 15:
                    c += 1
                if value >= 15 and value < 20:
                    d += 1
                if value >= 20:
                    e += 1
                nuclei_num += 1
            image_num += 1
            f.close()
    else:
        for filename in new_list_of_files:
            f = open(os.path.join(folder_name, filename),"r")
            csv_reader = csv.reader(f, delimiter = '\t')
            next(csv_reader)
            nuclei_num = 0

            for row in csv_reader:
                value = int(float(row[0].split(",")[5])/255)

                if not nuclei_num in exclusions[image_num]:
                    foci[image_num].append(value)
                    total_foci += value
                    total_nuclei += 1
                    if value < 5:
                        a += 1
                    if value >= 5 and value < 10:
                        b += 1
                    if value >= 10 and value < 15:
                        c += 1
                    if value >= 15 and value < 20:
                        d += 1
                    if value >= 20:
                        e += 1
                nuclei_num += 1
            image_num += 1
            f.close()
    return foci, num_of_files, total_foci, total_nuclei, a, b, c, d, e

def build_csv(foci_1, foci_2, coloc, option,i):
    csvfile = open('Condition ' + str(i + 1) + '.csv', 'w')
    writer = csv.writer(csvfile)
    writer.writerow(("Image No.", "Nuclei No.", "Telo Foci", "2nd Foci", "Colocalization"))
    for image_num in range(0, len(foci_1[0])):
        for nuclei_num in range(0, len(foci_1[0][image_num])):
            if nuclei_num == 0:
                writer.writerow((image_num +1, nuclei_num + 1, foci_1[0][image_num][nuclei_num], foci_2[0][image_num][nuclei_num], coloc[0][image_num][nuclei_num]))
            else:
                writer.writerow(("", nuclei_num + 1, foci_1[0][image_num][nuclei_num], foci_2[0][image_num][nuclei_num], coloc[0][image_num][nuclei_num]))
    writer.writerow(("","Total Nuclei",foci_1[3]))
    writer.writerow(("", "Avg No. Foci", float(foci_1[2])/foci_1[3], float(foci_2[2])/foci_2[3], float(coloc[2])/coloc[3]))
    if option == "Yes":
        writer.writerow(("", "[0,5)", "", float(foci_2[4])/foci_2[3], float(coloc[4])/coloc[3]))
        writer.writerow(("", "[5,10)", "", float(foci_2[5])/foci_2[3], float(coloc[5])/coloc[3]))
        writer.writerow(("", "[10,15)", "", float(foci_2[6])/foci_2[3], float(coloc[6])/coloc[3]))
        writer.writerow(("", "[15,20)", "", float(foci_2[7])/foci_2[3], float(coloc[7])/coloc[3]))
        writer.writerow(("", "[20, inf)", "", float(foci_2[8])/foci_2[3], float(coloc[8])/coloc[3]))

        writer.writerow(("", ">=5", "", 1 - float(foci_2[4])/foci_2[3], 1 - float(coloc[4])/coloc[3]))
        writer.writerow(("", ">=10", "", 1 - float(foci_2[4] + foci_2[5])/foci_2[3], 1 - float(coloc[4] + coloc[5])/coloc[3]))
        writer.writerow(("", ">=15", "", 1 - float(foci_2[4] + foci_2[5] + foci_2[6])/foci_2[3], 1 - float(coloc[4] + coloc[5] + coloc[6])/coloc[3]))
        writer.writerow(("", ">=20", "", float(foci_2[8])/foci_2[3], float(coloc[8])/coloc[3]))
    else:
        writer.writerow(("", "[0,5)", float(foci_1[4])/foci_1[3], float(foci_2[4])/foci_2[3], float(coloc[4])/coloc[3]))
        writer.writerow(("", "[5,10)", float(foci_1[5])/foci_1[3], float(foci_2[5])/foci_2[3], float(coloc[5])/coloc[3]))
        writer.writerow(("", "[10,15)", float(foci_1[6])/foci_1[3], float(foci_2[6])/foci_2[3], float(coloc[6])/coloc[3]))
        writer.writerow(("", "[15,20)", float(foci_1[7])/foci_1[3], float(foci_2[7])/foci_2[3], float(coloc[7])/coloc[3]))
        writer.writerow(("", "[20, inf)", float(foci_1[8])/foci_1[3], float(foci_2[8])/foci_2[3], float(coloc[8])/coloc[3]))

        writer.writerow(("", ">=5", 1 - float(foci_1[4])/foci_1[3], 1 - float(foci_2[4])/foci_2[3], 1 - float(coloc[4])/coloc[3]))
        writer.writerow(("", ">=10", 1 - float(foci_1[4] + foci_1[5])/foci_1[3], 1 - float(foci_2[4] + foci_2[5])/foci_2[3], 1 - float(coloc[4] + coloc[5])/coloc[3]))
        writer.writerow(("", ">=15", 1 - float(foci_1[4] + foci_1[5] + foci_1[6])/foci_1[3], 1 - float(foci_2[4] + foci_2[5] + foci_2[6])/foci_2[3], 1 - float(coloc[4] + coloc[5] + coloc[6])/coloc[3]))
        writer.writerow(("", ">=20", float(foci_1[8])/foci_1[3], float(foci_2[8])/foci_2[3], float(coloc[8])/coloc[3]))
    csvfile.close()


#test code
msg = "Do you want to exclude nuclei with less than 20 telomeric foci?"
choices = ["Yes", "No", "Exit"]
option = easygui.buttonbox(msg, choices=choices)

if option == "Exit":
    exit()

num = easygui.integerbox("How many different conditions?")

starting_num = [0]

if num > 1:
    for i in range(1, num):
        starting_num.append(easygui.integerbox("What is the starting number for the " + str(i+1) + "th condition?") - 1)

print(starting_num)

msg ="Which one is your Telo folder?"
title = "Telo Folder"
choices = os.listdir(os.curdir)
telo_foci = easygui.choicebox(msg, title, choices)

msg ="Which one is your 2nd foci folder?"
title = "2nd Foci Folder"
choices = os.listdir(os.curdir)
second_foci = easygui.choicebox(msg, title, choices)

msg ="Which one is your colocolization folder?"
title = "Colocalization Folder"
choices = os.listdir(os.curdir)
coloc_foci = easygui.choicebox(msg, title, choices)

for i in range(0, num):
    if option == "Yes":
        foci_1 = telo(telo_foci, i, starting_num)
        foci_2 = foci(second_foci, foci_1[4], i, starting_num, )
        coloc = foci(coloc_foci, foci_1[4], i, starting_num, )

    else:
        foci_1 = foci(telo_foci, [], i, starting_num)
        foci_2 = foci(second_foci, [], i, starting_num)
        coloc = foci(coloc_foci, [], i, starting_num)

    print(foci_1[1::])
    print(foci_2[1::])
    print(coloc[1::])

    build_csv(foci_1, foci_2, coloc, option, i)
