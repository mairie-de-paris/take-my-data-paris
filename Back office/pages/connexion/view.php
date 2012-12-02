<?php

/**
 * Copyright 2012-2013 The Apache Software Foundation
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
**/

defined('EXEC') or die('Access forbidden');

?>
<form method="post" class="connexion" action="<?= Url::toConnection(); ?>">
    <h3>formulaire de connexion</h3>
    <br />
    <p>
        <label>Pseudo : <input type="text" name="Upseudo" value="<?= $this->pseudo; ?>" /></label>
        <br /><br/>
        <label>Passwd : <input type="password" name="Upasswd" /></label>
        <br /><br />
        <input type="submit" name="connexion" value="Connexion" />
    </p>
</form>
<br /><br />
