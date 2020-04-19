import csv
with open('tst.csv', 'w') as csvfile:
    writer = csv.writer(csvfile)
    writer.writerows([['aa', 'bb', 'cc'],
                      ["adfaf", "nioub ", "dfadsf"]])