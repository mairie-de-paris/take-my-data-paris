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
<form method="post" action="<?= Url::toDelUser($this->user); ?>">
    <div style="width: 100%; text-align: center; color: #F00; font-weight: bold">
        Vous &ecirc;tes sur le point de supprimer un utilisateur.<br />
        Cette op&eacute;ration est IRREVERSIBLE !<br />
        <em>Aucun meurtre ne sera commis lors de cette op&eacute;ration.</em>
        <br /><br />
        Pour confirmer l'op&eacute;ration, veuillez renseigner votre mot de passe.
        <br /><br />
        <input type="password" name="Upasswd" /><input type="hidden" name="UID" value="<?= intval($this->user); ?>" />
        <br /><br />
        <input type="submit" value="Supprimer l'utilisateur" name="supprimer" />
    </div>
</form>