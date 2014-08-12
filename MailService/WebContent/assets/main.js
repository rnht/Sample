function sendClicked()
{
    if (checkForm())
    {
    $.ajax({
    type: 'POST',
    url: 'MailServiceServlet',
    data: $("#the-form").serializeArray(),
    success: function (resp) {
    	alertify.alert("Your email has been sent!");
    	$('#the-form').trigger("reset");;
    },
    error: function (xhr, ajaxOptions, thrownError) {
    	alertify.log("An error has occurred", "", 0);
    }
    });
    }
}

function checkForm()
{
    var isValid = true;
    $("input").each(function() {
        var element = $(this);
        if (element.val().length == 0 || element.val() == "") {
            isValid = false;
        }
    });
    if (! isValid)
    {
    	alertify.error("Please fill all fields.")
        return false;
    }
    else
    {
        if (!IsEmail($("#to").val()))
        {
        	alertify.error("Bad email address: " + $("#to").val());
            return false;
        }
        else if (!IsEmail($("#from").val()))
        {
        	alertify.error("Bad email address: " + $("#from").val());
            return false;
        }
    }
    return true;
}

function IsEmail(email) {
  var regex = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;
  return regex.test(email);
}
           
           
function goBack()
{
    moveDown();
}

function newEmail()
{
    document.getElementById("the-form").reset();
    goBack();
}

function moveUp()
{
    var a = document.getElementById("email");
    var b = document.getElementById("response");
    a.classList.remove("trans1-back");
    a.classList.add("trans1");
    b.classList.remove("trans2-back");
    b.classList.add("trans2");
}

function moveDown()
{
    var a = document.getElementById("email");
    var b = document.getElementById("response");
    a.classList.remove("trans1");
    a.classList.add("trans1-back");
    b.classList.remove("trans2");
    b.classList.add("trans2-back");
}