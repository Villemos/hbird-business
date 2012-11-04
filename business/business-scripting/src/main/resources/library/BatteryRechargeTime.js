var rechargeTime = (capacity.asDouble() * (1+(lostCharge.asDouble()/100)))/chargerOutput.asDouble() * 3600;
output.setValue(rechargeTime);
