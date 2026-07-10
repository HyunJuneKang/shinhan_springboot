$(function () {
    //이벤트위임
    $("#btnAll").on("click", f_selectAll);
    $("#btnNew").on("click", f_showForm);
    $("#btnCancel").on("click", f_hideForm);
    $("#empBody").on("click", ".btnEdit", f_detail);
    $("#empBody").on("click", ".btnDel", f_delete);
    $("#btnSave").on("click", f_insertUpdate);
    $("#btnSearch").on("click", f_search);
    $("#btnCondition").on("click", f_condition);
    //event call
    f_selectAll()
    f_deptList();
    f_jobList();
});

function f_condition() {
    const obj = {};

    const empName = $("#searchName").val().trim();
    const departmentId = $("#dept_id2").val();
    const jobId = $("#job_id2").val();
    const minSalary = $("#salary2").val();
    const hireDate = $("#hire_date2").val();

    if (empName) obj.empName = empName;
    if (departmentId) obj.departmentId = departmentId;
    if (jobId) obj.jobId = jobId;
    if (minSalary) obj.minSalary = minSalary;
    if (hireDate) obj.hireDate = hireDate;

    $.ajax({
        url: "/emp/condition",
        type: "POST",
        data: obj,
        dataType: "json",
        success: function (data) {
            renderData(data);
        },
        error: function () {
            alert("조건 조회 실패");
        }
    });
}

function f_jobList() {
    $.ajax({
        url: `/emp/job/list`,
        type: "GET",
        dataType: "json",
        success: function (jobList) {
            let output = `<option value="">전체</option>`;
            $.each(jobList, function (index, jobList) {
                output += `<option>
     	    		        ${jobList.jobId}</option>`;
            });
            $("#job_id2").append(output);
        },
        error: function () {
            alert("조건 조회 실패");
        }
    });
}

function f_deptList() {
    $.ajax({
        url: `/emp/dept/list`,
        type: "GET",
        dataType: "json",
        success: function (deptList) {
            let output = `<option value="">전체</option>`;
            $.each(deptList, function (index, dept) {
                output += `<option value="${dept.departmentId}">
     	    		        ${dept.departmentId} : ${dept.departmentName}</option>`;
            });
            $("#dept_id2").append(output);
        },
        error: function () {
            alert("조건 조회 실패");
        }
    });
}

function f_search() {
    const fname = $("#searchName").val().trim();
    if (!fname) {
        alert("이름을 입력해주세요");
        return;
    }
    $.ajax({
        url: `/emp/search`,
        type: "POST",
        data: {fname: fname},
        dataType: "json",
        success: function (data) {
            renderData(data);
        },
        error: function () {
            alert("조건 조회 실패");
        }
    });
}


function f_delete() {
    const empid = $(this).data("empid");//data-empid
    $.ajax({
        url: `/emp/delete`,
        data: {"empid": empid},
        type: "POST",
        dataType: "json",
        success: function (data) {
            alert(data + "건 삭제완료");
            $("#btnAll").click();
        }
    });
}

function f_insertUpdate() {
    const emp2 = $("#myfrm").serializeArray();
    const emp3 = {};
    $.each(emp2, function (index, item) {
        emp3[item.name] = item.value;
    });
    const empid = $("#employee_id").attr("data-empid");
    let url = "";
    let msg = "";

    if (empid) {
        url = "/emp/update";
        msg = "수정 완료";
    } else {
        url = "/emp/insert";
        msg = "등록 완료";
    }
    $.ajax({
        url: url,
        data: emp3,
        type: "POST",
        dataType: "json",
        success: function () {
            alert(msg);
            f_selectAll();
            f_hideForm();
        },
        error: function () {
            alert("저장 실패");
        }
    });
}

function f_detail() {
    const empid = $(this).data("empid");
    $.ajax({
        url: `/emp/detail`,
        data: {"empid": empid},
        type: "get",
        dataType: "json",
        success: function (data) {
            renderFormBox(data);
        }
    });
}

function renderFormBox(emp) {
    $("#employee_id").val(emp.employeeId);
    $("#employee_id").attr("data-empid", emp.employeeId);
    $("#first_name").val(emp.firstName);
    $("#last_name").val(emp.lastName);
    $("#email").val(emp.email);
    $("#department_id").val(emp.departmentId);
    $("#salary").val(emp.salary);
    $("#hire_date").val(emp.hireDate);
    $("#job_id").val(emp.jobId);
    $("#formBox").show();
}

function formatDate(dateStr) {
    //6월 17, 2003  => ["6", "17", "2003"]
    const nums = dateStr.match(/(\d+)/g);
    const month = nums[0].padStart(2, "0"); //"06"
    const day = nums[1].padStart(2, "0"); //"17"
    const year = nums[2];
    return `${year}-${month}-${day}`;
}

function f_hideForm() {
    $("#formBox").hide();
}

function f_showForm() {
    $("#employee_id").attr("data-empid", "");
    $("#employee_id,#first_name,#last_name,#email,#job_id,#department_id,#salary, #hire_date").val("");
    $("#formBox").show();
}

function f_selectAll() {
    $.ajax({
        url: `/emp/list`,
        type: "GET",
        dataType: "json",
        success: function (data) {
            renderData(data);
        }
    });
}

function renderData(empList) {
    const tbody = $("#empBody");
    tbody.empty();
    if (empList.length === 0) {
        tbody.append("<tr><td colspan='6'>조회한 데이터가 없습니다.</td></tr>");
        return;
    }
    $.each(empList, function (index, emp) {
        const empRow = `
    		    <tr>
    		      <td>${emp.employeeId}</td>
    		      <td>${emp.firstName} ${emp.lastName}</td>
    		      <td>${emp.jobId}</td>
    		      <td>${emp.departmentId}</td>
    		      <td>${emp.salary.toLocaleString()}</td>
    		      <td>${emp.jobId}</td>
    		      <td>${emp.hireDate}</td>
    		      <td>
    		        <button class="btnEdit" 
    		           data-empid="${emp.employeeId}">detail</button>
    		        <button class="btnDel" data-empid="${emp.employeeId}">del</button>
    		      </td>
    		    </tr>
    		`;
        tbody.append(empRow);
    });

}