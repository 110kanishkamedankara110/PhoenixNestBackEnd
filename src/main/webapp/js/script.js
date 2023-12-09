function login() {
    let email = document.getElementById("email");
    let password = document.getElementById("password");

    let auth = {};
    auth.email = email.value;
    auth.password = password.value;

    fetch(
        "/PhoenixNestBackEnd/admin/login",
        {
            method: "POST",
            body: JSON.stringify(auth),
            headers: {
                'Content-Type': 'application/json',
            },
        }
    ).then(resp => {
        return resp.json();
    }).then(json => {
        if (json.message == "Success") {
            window.location = "/PhoenixNestBackEnd/admin/Home";
        }
    })


}

function loadTable() {

    fetch(
        "/PhoenixNestBackEnd/api/app/getApps",
        {
            method: "GET",

        }
    ).then(resp => {
        return resp.json();
    }).then(json => {
        let body = document.getElementById("body");
        let table = document.createElement("table");
        table.border = 1;
        table.style.borderCollapse = "collapse";

        let trHeader = document.createElement("tr");
        let thTitle = document.createElement("th");
        let thDescription = document.createElement("th");
        let thPackageName = document.createElement("th");
        let thDownloads = document.createElement("th");
        let thCategoryies = document.createElement("th");
        let thActive = document.createElement("th");
        let thReleases = document.createElement("th");


        thTitle.innerHTML = "App Title";
        thDescription.innerHTML = "App Description";
        thPackageName.innerHTML = "Package Name";
        thDownloads.innerHTML = "Downloads";
        thCategoryies.innerHTML = "Categoryies";
        thActive.innerHTML = "Active";
        thReleases.innerHTML = "Releases";


        trHeader.appendChild(thTitle);
        trHeader.appendChild(thDescription);
        trHeader.appendChild(thPackageName);
        trHeader.appendChild(thDownloads);
        trHeader.appendChild(thCategoryies);
        trHeader.appendChild(thActive);
        trHeader.appendChild(thReleases);


        table.appendChild(trHeader);

        json.forEach(j => {
            let tr = document.createElement("tr");
            let tdTitle = document.createElement("td");
            let tdDescription = document.createElement("td");
            let tdPackageName = document.createElement("td");
            let tdDownloads = document.createElement("td");
            let tdCategoryies = document.createElement("td");
            let tdActive = document.createElement("td");
            let tdRel = document.createElement("td");

            tdTitle.innerHTML = j.appTitle;
            tdDescription.innerHTML = j.description;
            tdPackageName.innerHTML = j.packageName
            tdDownloads.innerHTML = j.downloads
            tdActive.innerHTML = j.active;
            if (j.appReleaseDtoList != null) {

                j.appReleaseDtoList.forEach(re => {
                    let url = "/PhoenixNestBackEnd/api/app/download/" + j.packageName + "/" + re.versionCode + "/" + re.apk
                    let version = re.version;
                    let packageName = j.packageName;
                    let versionCode = re.versionCode;
                    if (!re.approved) {
                        tdRel.innerHTML = tdRel.innerHTML + " " + re.version + " (" + re.versionCode + ") Approved :" + re.approved + "<a href='" + url + "'>Download</a><button onclick=approve('" + version + "','" + packageName + "','" + versionCode + "')>Approve</button></br>";

                    } else {
                        tdRel.innerHTML = tdRel.innerHTML + " " + re.version + " (" + re.versionCode + ") Approved :" + re.approved + "<a href='" + url + "'>Download</a></br>";

                    }


                })
            } else {
                tdRel.innerHTML = "";
            }
            if (j.categoryies != null) {

                j.categoryies.forEach(re => {

                    tdCategoryies.innerHTML = tdCategoryies.innerHTML + " " + re + "</br>";

                })
            } else {
                tdCategoryies.innerHTML = "";
            }


            tr.appendChild(tdTitle);
            tr.appendChild(tdDescription);
            tr.appendChild(tdPackageName);
            tr.appendChild(tdDownloads);
            tr.appendChild(tdCategoryies);
            tr.appendChild(tdActive);
            tr.appendChild(tdRel);


            table.appendChild(tr);


        })


        table.style.width = "100%";
        table.style.border = "1px solid black"
        body.appendChild(table);

    })


}

function approve(version, packageName, versionCode) {
    alert(version + " " + packageName + "" + versionCode);

    fetch(
        "/PhoenixNestBackEnd/api/app/approveRelease/" + packageName + "/" + version + "/" + versionCode,
        {
            method: "POST",

        }
    ).then(resp => {
        return resp.json();
    }).then(json => {
        if (json.message == "Success") {
            window.location.reload();
        }
    });


}