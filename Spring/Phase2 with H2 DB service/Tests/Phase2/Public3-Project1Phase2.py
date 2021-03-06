from http import HTTPStatus
from threading import Thread
import requests

# Check if consistency is preserved in Add Balance

# RESTAURANT SERVICE    : http://localhost:8080
# DELIVERY SERVICE      : http://localhost:8081
# WALLET SERVICE        : http://localhost:8082


def t1(result):  # First concurrent request
    # Customer 301 is added with 100 amount
    http_response = requests.post(
        "http://localhost:8082/addBalance", json={"custId": 301, "amount": 100})
    result["1"] = http_response


def t2(result):  # Second concurrent request
    # Customer 301 is added with 100 amount
    http_response = requests.post(
        "http://localhost:8082/addBalance", json={"custId": 301, "amount": 100})
    result["2"] = http_response


def test():

    result = {}

    # Reinitialize Restaurant service
    http_response = requests.post("http://localhost:8080/reInitialize")

    # Reinitialize Delivery service
    http_response = requests.post("http://localhost:8081/reInitialize")

    # Reinitialize Wallet service
    http_response = requests.post("http://localhost:8082/reInitialize")

    # Agent 201 sign in
    http_response = requests.post(
        "http://localhost:8081/agentSignIn", json={"agentId": 201})

    if(http_response.status_code != HTTPStatus.CREATED):
        return 'Fail1'

    ### Parallel Execution Begins ###
    thread1 = Thread(target=t1, kwargs={"result": result})
    thread2 = Thread(target=t2, kwargs={"result": result})

    thread1.start()
    thread2.start()

    thread1.join()
    thread2.join()

    ### Parallel Execution Ends ###

    status_code1 = result["1"].status_code
    status_code2 = result["2"].status_code

    if (status_code1 != status_code2 and status_code2 != HTTPStatus.CREATED):
        return "Fail2"

    # Check customer resulting amount
    http_response = requests.get(
        f"http://localhost:8082/balance/301")

    if(http_response.status_code != HTTPStatus.OK):
        return 'Fail3'

    res_body = http_response.json()

    balance = res_body.get("balance")
    if(balance != 2200.0):
        return 'Fail4'

    return 'Pass'


if __name__ == "__main__":
    print(test())
