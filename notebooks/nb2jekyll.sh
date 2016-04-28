# To 

if [ "$#" -eq 1 ]; then
    jupyter nbconvert --to markdown $1
else
    echo "First argument: notebook file"
fi

